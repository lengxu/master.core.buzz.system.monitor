package cn.uyun.service.product;

import cn.uyun.dao.es.EsOperation;
import cn.uyun.entity.ProductInfo;
import cn.uyun.entity.config.Config;
import cn.uyun.entity.dataProcess.CurrentData;
import cn.uyun.entity.dataProcess.FileBean;
import cn.uyun.entity.dataProcess.ProductBean;
import cn.uyun.entity.dataProcess.ProductResultBean;
import cn.uyun.util.DateTool;
import cn.uyun.util.OKHttpTools;
import com.alibaba.fastjson.JSON;
import com.google.gson.JsonArray;
import io.searchbox.core.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class ProductServiceAbstract extends ProductInfo {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceAbstract.class);

    @Autowired
    protected EsOperation esOperation;

    @Autowired
    protected OKHttpTools okHttpTools;

    @Autowired
    protected Config config;

    private static final String _INDEX = "atlantic_log_e10adc3949ba59abbe56e057f20f88dd_";

    private static String prefix = "{\"query\": {\"bool\": {\"must\": [{\"term\": {\"SYSTEM\": \"ART\"}},";

    private static String stuffix = "]}}}";

    //总览数据缓存
    public static HashMap currentDataMap = new HashMap();

    //详情数据缓存
    public static HashMap<String, LinkedList> detailMap = new HashMap<>();

    /**
     * 产品生成、收集、入库数据查询的总入口，通过param参数来区分是产品生成还是收集或者入库
     */
    public void queryProductData(String param) {
        LinkedList<Object> productList = new LinkedList<>();

        //封装总览数据
        ArrayList arrayList = new ArrayList();

        //遍历接入的所有资料
        for (int i = 0; i < productResultBeans.size(); i++) {
            ProductResultBean productResultBean = productResultBeans.get(i);
            ArrayList list = new ArrayList();
            CurrentData currentData = new CurrentData();
            list.add(currentData);
            productList.add(execute(param, productResultBean, arrayList));
        }

        //总览数据
        currentDataMap.put(param, arrayList);

        //详情数据
        detailMap.put(param, productList);
    }

    /*
     * 产品生成环节：1.产品生成环节的的业务时次为北京时，服务器为世界时。因此receive_timr过滤时需要进行减8
     * 收集和入库环节：1.从cts环节查询出资料的收集环节、入库环节的应收数据。
     *               2.通过cts、sod、dpc编码及时次。调用mcp接口查询实际入库的文件名称。
     *               3.
     * */
    public Object execute(String param, ProductResultBean productResultBean, ArrayList arrayList) {
        Map<String, Object> productMap = new HashMap<String, Object>();
        LinkedList<ProductBean> productBeanList = new LinkedList<>();

        String nowDate = DateTool.getSysTime("yyyy-MM-dd");

        String cts = productResultBean.getCts();
        String dpc = productResultBean.getDpc();
        String sod = productResultBean.getSod();

        int fileNumber = 0;
        if ("小时".equals(productResultBean.getProductType())) {
            hourList = new String[]{"00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"};
            title = new String[]{"1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "24:00"};
            fileNumber = productResultBean.getFileNumber();
        } else if ("日".equals(productResultBean.getProductType())) {
            if (productResultBean.getTime().equals("08")) {
                hourList = new String[]{"08:00", "20:00"};
                title = new String[]{"8:00", "20:00"};
            } else if (productResultBean.getTime().equals("10")) {
                hourList = new String[]{"10:00"};
                title = new String[]{"10:00"};
            } else if (productResultBean.getTime().equals("8")) {
                hourList = new String[]{"08:00"};
                title = new String[]{"8:00"};
            }
            fileNumber = productResultBean.getFileNumber() / 2;
        } else if ("分钟".equals(productResultBean.getProductType())) {
            hourList = new String[]{"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
            title = new String[]{"1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "24:00"};
            mintinus = new String[]{"00", "10", "20", "30", "40", "50"};
            fileNumber = productResultBean.getFileNumber() * 6;
        }

        JsonArray asJsonArray = getDiFromEs(productResultBean, param, nowDate);

        CurrentData currentData = null;


        Map<String, List<FileBean>> fileBeanMap = productResultBean.getFileBean();

        //status  //默认为0 灰色
        for (int i = 0; i < hourList.length; i++) {  //循环获取24个小时的数据
            //封装资料各个时次返回的数据
            ProductBean productBean = new ProductBean(cts, dpc, sod, "", 0, fileNumber, "0", "");

            //当当前时刻小于查询的时次，则后面所有时次的状态及应收为默认值，不去调用mcp接口查询。(小时数据及分钟数据是按照北京时)
            long lo = DateTool.StringToDate(nowDate + " " + hourList[i] + (mintinus == null ? "" : ":00"), "yyyy-MM-dd HH:mm").getTime();
            lo = (lo - 8 * 60 * 60 * 1000);

            //返回字段新增预报时次
            try {
                //由于详情页面按北京时显示，而10分钟与小时数据时次为北京时，日、海表温度数据时次为世界时，因此需要对日和海表温度时次数据进行单独处理
                String forcastTime = null;
                String datehour = "分钟".equals(dataType) ? nowDate + " " + hourList[i] + ":00" : nowDate + " " + hourList[i];
                forcastTime = datehour.trim().substring(datehour.length() - 5, datehour.length());
                productBean.setForcastTime(forcastTime);
                productBean.setTime(title[i]);
            } catch (Exception e) {
                LOGGER.error("转换预报时次为世界时异常！");
                e.printStackTrace();
            }

            if(System.currentTimeMillis() > lo) {
                if (param.equals("Cre")) {
                    if (mintinus != null) {
                        //10分钟资料
                        getCreMiniteData(asJsonArray, nowDate, productBean, hourList[i], mintinus);
                    } else {
                        getCreDate(asJsonArray, nowDate, hourList[i] + ":00", productBean);
                    }
                } else {
                    if ("分钟".equals(dataType)) {
                        //10分钟
                        getMiniteData(param, asJsonArray, nowDate, hourList[i], productBean, fileBeanMap, tTime, mintinus);
                    } else if ("小时".equals(dataType)) {
                        //小时
                        getHourData(param, asJsonArray, nowDate, hourList[i], productBean, fileBeanMap, tTime);
                    } else if ("日".equals(dataType)) {
                        //日
                        getDayData(param, nowDate, hourList[i], productBean, fileBeanMap, tTime);
                    }
                }
                currentData = new CurrentData();
                currentData.setApi_data_code(productResultBean.getCts());
                currentData.setForcastTime(productBean.getForcastTime());
                currentData.setMusic(productResultBean.getMusic_code());
                currentData.setName(productResultBean.getName());
                currentData.setNowStatus(productBean.getStatus());
            }
            productBeanList.add(productBean);
        }

        if(currentData != null) {
            arrayList.add(currentData);
        }

        //封装返回内容
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
        String cts = productResultBean.getCts();

        //生成查询的indexs
        LinkedHashSet indexs = new LinkedHashSet();
        String str = null;
        String type = null;

        if (param.equals("Cre")) {
            long nowDayTime = DateTool.getNowDayTime(nowDate);
            long time = "全球海表温度".equals(productResultBean.getName()) ? nowDayTime : nowDayTime - 8 * 3600 * 1000;
            str = prefix + "{\"term\": {\"DATA_TYPE\": \"" + cts + "\"}},{\"range\":{\"@receive_time\":{\"gt\": " + time + "}}}" + stuffix;
            //如果是全球海表温度，由于其今天推送的数据存到昨天的索引，因此获取前一天数据
            indexs.add(_INDEX + DateTool.getSysTime("yyyyMMdd"));
            indexs.add(_INDEX + DateTool.getNowDateBefore("yyyyMMdd", Calendar.DATE, -1));
            type = "OT.DATA.PRODUCT.DI";
        } else {
            Long startTime = DateTool.StringToDate(nowDate + " 00:00:00", "yyyy-MM-dd HH:mm:ss").getTime();
            Long endTime = DateTool.StringToDate(nowDate + " 23:59:59", "yyyy-MM-dd HH:mm:ss").getTime();
            str = "{\"query\": {\"bool\": {\"must\": [{\"match_phrase\": {\"cts\": \"" + cts + "\"}},{\"range\": {\"ltime\": {\"gte\":" + (startTime -  8 * 3600 * 1000) + ",\"lte\":" + (endTime -  8 * 3600 * 1000) + "}}}]}},\"_source\": {\"includes\": [\"time\",\"idTdNum\",\"idActualNum\",\"idRateState\",\"coTdNum\",\"coActualNum\",\"coRateState\",\"cts\",\"dpc\",\"sod\",\"ltime\",\"idRateState\"],\"excludes\": []}}";
            indexs.add("demo");
        }
        SearchResult searchResult = esOperation.queryDocuments(indexs, type, str, 10000);
        JsonArray asJsonArray = searchResult.getJsonObject().get("hits").getAsJsonObject().get("hits").getAsJsonArray();
        return asJsonArray;
    }
}
