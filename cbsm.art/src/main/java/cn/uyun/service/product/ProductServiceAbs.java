package cn.uyun.service.product;


import cn.uyun.dao.es.EsOperation;
import cn.uyun.entity.ProductInfo;
import cn.uyun.entity.config.Config;
import cn.uyun.entity.dataProcess.CurrentData;
import cn.uyun.entity.dataProcess.ProductBean;
import cn.uyun.entity.dataProcess.ProductResultBean;
import cn.uyun.service.product.env.ProductCO;
import cn.uyun.service.product.env.ProductCre;
import cn.uyun.util.DateTool;
import com.alibaba.fastjson.JSON;
import com.google.gson.JsonArray;
import io.searchbox.core.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author wuhan
 * @date 2019-10-21
 */
@Component
public class ProductServiceAbs extends ProductInfo {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceAbs.class);

    //总览数据缓存
    public static HashMap currentDataMap = new HashMap();

    //详情数据缓存
    public static HashMap<String, LinkedList> detailMap = new HashMap<>();

    @Autowired
    private Config config;

    @Autowired
    protected EsOperation esOperation;

    @Autowired
    protected ProductCre productCre;

    @Autowired
    private ProductCO productCO;

    private static final String _INDEX = "atlantic_log_e10adc3949ba59abbe56e057f20f88dd_";

    private static String prefix = "{\"query\": {\"bool\": {\"must\": [{\"term\": {\"SYSTEM\": \"ART\"}},";

    private static String stuffix = "]}}}";

    /**
     * 产品生成、收集、入库数据查询的总入口，通过param参数来区分是产品生成还是收集或者入库
     */
    public void queryProductData(String param) {
        LinkedList<Object> detailList = new LinkedList<>();

        //封装总览数据
        ArrayList currentList = new ArrayList();

        //遍历获取接入的所有资料数据
        for (int i = 0; i < productResultBeans.size(); i++) {
            ProductResultBean productResultBean = productResultBeans.get(i);
            detailList.add(execute(param, productResultBean, currentList));
        }

        //总览数据
        currentDataMap.put(param, currentList);

        //详情数据
        detailMap.put(param, detailList);
    }

    //比对获取ES中每个时次最后一条DI
    public Object execute(String param, ProductResultBean productResultBean, ArrayList currentList) {
        String hourList[] = null;   //用来匹配从es中查询出来的数据的时次
        //用来返回给前端的业务时次进行展示
        String title[] = new String[]{"1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "24:00"};
        String mintinus[] = null;   //10分钟级别
        int fileNumber = productResultBean.getFileNumber();
        ;
        if (0 == productResultBean.getProductType()) {
            hourList = new String[]{"00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};
        } else if (1 == productResultBean.getProductType()) {
            hourList = new String[]{"08:00"};
            title = new String[]{"8:00"};
        } else if (2 == productResultBean.getProductType()) {
            hourList = new String[]{"10:00"};
            title = new String[]{"10:00"};
        } else if (3 == productResultBean.getProductType()) {
            hourList = new String[]{"08:00", "20:00"};
            title = new String[]{"8:00", "20:00"};
        } else if (4 == productResultBean.getProductType()) {
            hourList = new String[]{"10:00", "22:00"};
            title = new String[]{"10:00", "22:00"};
        } else if (5 == productResultBean.getProductType()) {
            hourList = new String[]{"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
            title = new String[]{"1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "24:00"};
            mintinus = new String[]{"00", "10", "20", "30", "40", "50"};
            fileNumber = productResultBean.getFileNumber() * 6;
        }

        String nowDate = DateTool.getSysTime("yyyy-MM-dd");
        JsonArray asJsonArray = getDiFromEs(productResultBean, param, nowDate);

        //status  //默认为0 灰色
        LinkedList<ProductBean> productBeanList = new LinkedList<>();
        LinkedList<ProductBean> productBeanCurrentList = new LinkedList<>();
        for (int i = 0; i < hourList.length; i++) {  //循环获取24个小时的数据
            //封装资料各个时次返回的数据
            ProductBean productBean = new ProductBean(productResultBean.getCts(), productResultBean.getDpc(), productResultBean.getSod(), "", 0, fileNumber, "0", "");

            //返回字段新增预报时次
            try {
                //天镜的全球海表温度DATA_DATE为世界时，大数据的为北京时
                String datehour = 5 == productResultBean.getProductType() ? nowDate + " " + hourList[i] + ":00" : nowDate + " " + hourList[i];
                String forcastTime = datehour.trim().substring(datehour.length() - 5, datehour.length());
                productBean.setForcastTime(forcastTime);
                productBean.setTime(title[i]);
            } catch (Exception e) {
                LOGGER.error("转换预报时次为世界时异常！");
                e.printStackTrace();
            }

            //当当前时刻小于查询的时次，则后面所有时次的状态及应收为默认值，不去调用mcp接口查询。(小时数据及分钟数据是按照北京时)
            long lo = DateTool.StringToDate(nowDate + " " + hourList[i] + (mintinus == null ? "" : ":00"), "yyyy-MM-dd HH:mm").getTime();
            lo = (lo - 8 * 60 * 60 * 1000);
            if (System.currentTimeMillis() > lo) {
                if (param.equals("Cre")) {
                    if (mintinus != null) {
                        //10分钟资料
                        productCre.getCreMiniteData(asJsonArray, nowDate, productBean, hourList[i], mintinus);
                    } else {
                        productCre.getCreDate(asJsonArray,nowDate, hourList[i] + ":00", productBean);
                    }
                } else {
                    if (5 == productResultBean.getProductType()) {
                        //10分钟
                        productCO.getMiniteData(param, asJsonArray, nowDate, hourList[i], productBean, productResultBean.getFileBean(), null, mintinus);
                    } else if (0 == productResultBean.getProductType()) {
                        //小时
                        productCO.getHourData(param, asJsonArray, nowDate, hourList[i], productBean, productResultBean.getFileBean(), null);
                    } else {
                        //日
                        productCO.getDayData(param, asJsonArray, nowDate, hourList[i], productBean, productResultBean.getFileBean(), null);
                    }
                }
                productBeanCurrentList.add(productBean);
            }
            productBeanList.add(productBean);
        }

        ProductBean productBean = productBeanCurrentList.getLast();
        CurrentData currentData = new CurrentData();
        currentData.setForcastTime(productBean.getForcastTime());
        currentData.setNowStatus(productBean.getStatus());
        currentData.setApi_data_code(productResultBean.getCts());
        currentData.setMusic(productResultBean.getMusic_code());
        currentData.setName(productResultBean.getName());
        currentList.add(currentData);

        //封装返回内容
        Map<String, Object> productMap = new HashMap<String, Object>();
        productMap.put("name", productResultBean.getName());
        productMap.put("music", productResultBean.getMusic_code());
        productMap.put("api_data_code", productResultBean.getCts());
        productMap.put("values", productBeanList);
        LOGGER.info(productResultBean.getCts() + "详情页面数据：" + JSON.toJSONString(productBeanList));
        LOGGER.info("=============================================================================");
        return productMap;
    }

    //从ES中获取产品生成、收集、入库的DI,以便后续进行处理
    public JsonArray getDiFromEs(ProductResultBean productResultBean, String param, String nowDate){
        //生成查询的indexs
        LinkedHashSet indexs = new LinkedHashSet();
        String str = null;
        String type = null;

        if (param.equals("Cre")) {
            long nowDayTime = DateTool.getNowDayTime(nowDate);
            long time = "全球海表温度".equals(productResultBean.getName()) ? nowDayTime : nowDayTime - 8 * 3600 * 1000;
            str = prefix + "{\"term\": {\"DATA_TYPE\": \"" + productResultBean.getCts() + "\"}},{\"range\":{\"@receive_time\":{\"gt\": " + time + "}}}" + stuffix;
            //如果是全球海表温度，由于其今天推送的数据存到昨天的索引，因此获取前一天数据
            indexs.add(_INDEX + DateTool.getSysTime("yyyyMMdd"));
            indexs.add(_INDEX + DateTool.getNowDateBefore("yyyyMMdd", Calendar.DATE, -1));
            type = "OT.DATA.PRODUCT.DI";
        } else {
            indexs.add("atlantic_log_file_di_statistics_result_" + DateTool.getSysTime("yyyyMMdd"));
            str = "{\"query\": {\"bool\": {\"must\": [{\"term\": {\"CTS\": \"" + productResultBean.getCts() + "\"}},{\"term\": {\"SOD\": \"" + productResultBean.getSod() + "\"}}]}},\"_source\": {\"includes\": [\"CTS\",\"DATA_TIME\",\"CO_TD\",\"CO_ACTUAL\",\"ID_TD_"+config.getMcpDestination()+"\",\"ID_ACTUAL_"+config.getMcpDestination()+"\"],\"excludes\": []}}";
        }
        SearchResult searchResult = esOperation.queryDocuments(indexs, type, str, 10000);
        JsonArray asJsonArray = searchResult.getJsonObject().get("hits").getAsJsonObject().get("hits").getAsJsonArray();
        return asJsonArray;
    }
}
