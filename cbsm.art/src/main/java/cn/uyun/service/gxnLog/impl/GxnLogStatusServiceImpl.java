package cn.uyun.service.gxnLog.impl;

import cn.uyun.dao.es.EsOperation;
import cn.uyun.service.gxnLog.GxnLogStatusService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.searchbox.core.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 * @author wuhan
 * @date 2019-09-23
 */
@Service
public class GxnLogStatusServiceImpl implements GxnLogStatusService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GxnLogStatusServiceImpl.class);

    public String apiKey = "e10adc3949ba59abbe56e057f20f88dd";

    private String _type = "OT.DATA.PRODUCT.DI";

    private String[] timeLevel = new String[]{"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};

    @Autowired
    protected EsOperation esOperation;

    private static final String _INDEX = "atlantic_log_e10adc3949ba59abbe56e057f20f88dd_";

    private static String prefix = "{\"query\": {\"bool\": {\"must\": [{\"term\": {\"SYSTEM\": \"ART_HPCSG\"}},";

    private static String stuffix = "]}},\"sort\": [{\"@receive_time\": {\"order\": \"asc\"}}]}";

    public static LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, String>>> logCache = new LinkedHashMap<>();

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    @Override
    public String queryStatus(ArrayList<String> modelNames) {
        //封装公共查询ES库参数
        LinkedHashSet indcies = new LinkedHashSet();
        indcies.add(_INDEX + dateFormat.format(new Date()));

        //遍历七种模式名
        long sTime = System.currentTimeMillis();
        for (String modelName : modelNames) {
            String queryParam = "{\"term\": {\"suite_name\": \"" + modelName + "\"}}";
            SearchResult searchResult = esOperation.queryDocuments(indcies, _type, prefix + queryParam + stuffix, 1000);
            JsonArray asJsonArray = searchResult.getJsonObject().get("hits").getAsJsonObject().get("hits").getAsJsonArray();

            LinkedHashMap<String, LinkedHashMap<String, String>> linkedHashMap = new LinkedHashMap();
            for(String level : timeLevel){
                LinkedHashMap<String, String> initLinkedList = new LinkedHashMap<>();
                initLinkedList.put("receive_time", "0");
                initLinkedList.put("status", "queued");
                linkedHashMap.put(level, initLinkedList);
            }
            for (int t = 0; t < asJsonArray.size(); t++) {
                try {
                    JsonObject asJsonObject = asJsonArray.get(t).getAsJsonObject();
                    JsonObject source = asJsonObject.get("_source").getAsJsonObject();

                    String level = source.get("time_level").getAsString();
                    long time = source.get("@receive_time").getAsLong();
                    String status = source.get("status").getAsString();

                    if (time > Long.parseLong(linkedHashMap.get(level).get("receive_time"))) {
                        //说明该时次有最新的DI
                        linkedHashMap.get(level).put("receive_time", String.valueOf(time));
                        linkedHashMap.get(level).put("status", status);
                    }
                } catch (Exception e) {
                    LOGGER.error("解析DI失败,将跳过该条DI。 失败DI详情：" + asJsonArray.get(t).getAsString());
                    e.printStackTrace();
                }
            }
            logCache.put(modelName, linkedHashMap);
        }
        long eTime = System.currentTimeMillis();
        System.out.println("==============总耗时："+(eTime - sTime)+"==============");
        return JSON.toJSONString(logCache, SerializerFeature.DisableCircularReferenceDetect);
    }
}
