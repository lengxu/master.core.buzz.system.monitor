package cn.uyun.service.product.impl;

import cn.uyun.dao.es.EsOperation;
import cn.uyun.entity.config.Config;
import cn.uyun.entity.dataProcess.*;
import cn.uyun.service.product.ProductService;
import cn.uyun.util.DateTool;
import cn.uyun.util.OKHttpTools;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.searchbox.core.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author wuhan
 * @date 2019-05-06
 */
//@SuppressWarnings("all")
@Service
@SuppressWarnings("all")
public class ProductServiceImpl implements ProductService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
    private SimpleDateFormat _dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    protected static ArrayList<ProductResultBean> productResultBeans = new ArrayList<ProductResultBean>();

    /*
    * 产品生成环节：1.产品生成环节的的业务时次为北京时，服务器为世界时。因此receive_timr过滤时需要进行减8
    * 收集和入库环节：1.从cts环节查询出资料的收集环节、入库环节的应收数据。
    *               2.通过cts、sod、dpc编码及时次。调用mcp接口查询实际入库的文件名称。
    *               3.
    * */
    public Object execute(String param, ProductResultBean productResultBean, String dataType, String tTime, ArrayList arrayList) {
        Map<String, Object> productMap = new HashMap<String, Object>();
        LinkedList<ProductBean> productBeanList = new LinkedList<>();

        String nowDate = DateTool.getSysTime("yyyy-MM-dd");

        String cts = productResultBean.getCts();
        String dpc = productResultBean.getDpc();
        String sod = productResultBean.getSod();
        String hourList[] = null;
        String title[] = null;
        String mintinus[] = null;
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

    //产品生成小时、日数据
    public void getCreDate(JsonArray asJsonArray, String date, String hourAndMinite, ProductBean productBean) {
        //判断是否存在多条DI，如果存在多条，则取receive_time最新的一条
        for (int t = 0; t < asJsonArray.size(); t++) {
            try {
                JsonObject asJsonObject = asJsonArray.get(t).getAsJsonObject();
                JsonObject source = asJsonObject.get("_source").getAsJsonObject();

                //由于全球海表温度资料传递的是世界时，而其他8种资料都是北京时，这里针对全球海表温度进行调整，将其调整为世界时
                String data_date = source.get("DATA_DATE").getAsString();
                if ("C.0011.0001.R001".equals(productBean.getCts())) {
                    Date parse = null;
                    parse = _dateFormat.parse(data_date);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(parse);
                    calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + 8 + 1);
                    Date time = calendar.getTime();
                    data_date = simpleDateFormat.format(time);
                }

                if ((date + " " + hourAndMinite).equals(data_date)) {
                    //新增返回字段新增预报时次
                    productBean.setForcastTime(dateFormat.format(simpleDateFormat.parse(source.get("DATA_DATE").getAsString())));

                    productBean.setIdActualNum(source.get("FILE_NUM").getAsInt());    //FILE_NUM 已生成文件数
                    String state = source.get("DATA_STATE").getAsString();
                    //生成环节es库中的dataStatus字段对应的值为0、1、2.其中0代表正常、1和2代表异常。而前台0代表没数据，1代表绿、2代表红，因此这里得进行转换
                    if (state.equals("2") || state.equals("1")) { //红色
                        productBean.setStatus("2"); //默认为0 灰色, 1绿色，2红色
                        String detail = source.get("FILE_NAME").getAsString();
                        detail = "已生成如下资料：<br/>" + detail.replaceAll(",", "<br/>");
                        productBean.setDetail(detail);
                    } else if (state.equals("0")) { //灰色或者绿色
                        productBean.setStatus("1"); //默认为0 灰色
                    }
                }
            } catch (ParseException e) {
                LOGGER.error("解析DI失败,将跳过该条DI。 失败DI详情：" + JSON.toJSONString(productBean));
                e.printStackTrace();
            }
        }
    }

    //产品生成10分钟数据
    public void getCreMiniteData(JsonArray asJsonArray, String date, ProductBean productBean, String hour, String[] mintinus) {
        int num = 0;
        for (int k = 0; k < mintinus.length; k++) {
            for (int t = 0; t < asJsonArray.size(); t++) {
                try {
                    JsonObject asJsonObject = asJsonArray.get(t).getAsJsonObject();
                    JsonObject source = asJsonObject.get("_source").getAsJsonObject();

                    //由于全球海表温度资料传递的是世界时，而其他8种资料都是北京时，这里针对全球海表温度进行调整，将其调整为世界时
                    String data_date = source.get("DATA_DATE").getAsString();

                    if ((date + " " + hour + ":" + mintinus[k] + ":00").equals(data_date)) {
                        //新增返回字段新增预报时次
                        productBean.setForcastTime(dateFormat.format(simpleDateFormat.parse(source.get("DATA_DATE").getAsString())));
                        num += source.get("FILE_NUM").getAsInt();    //FILE_NUM 已生成文件数
                        String state = source.get("DATA_STATE").getAsString();
                        //生成环节es库中的dataStatus字段对应的值为0、1、2.其中0代表正常、1和2代表异常。而前台0代表没数据，1代表绿、2代表红，因此这里得进行转换
                        if (state.equals("2") || state.equals("1")) { //红色
                            productBean.setStatus("2"); //默认为0 灰色, 1绿色，2红色
                            String detail = source.get("FILE_NAME").getAsString();
                            detail = "已生成如下资料：<br/>" + detail.replaceAll(",", "<br/>");
                            productBean.setDetail(detail);
                        } else if (state.equals("0")) { //灰色或者绿色
                            productBean.setStatus("1"); //默认为0 灰色
                        }
                    }
                } catch (ParseException e) {
                    LOGGER.error("解析DI失败,将跳过该条DI。 失败DI详情：" + JSON.toJSONString(productBean));
                    e.printStackTrace();
                }
            }
            //productBean.setDate(date + " " + hour + ":" + mintinus[k]);
        }
        productBean.setIdActualNum(num);
    }

    //收集与入库10分钟数据
    private void getMiniteData(String param, JsonArray asJsonArray, String date, String hourList, ProductBean productBean, Map<String, List<FileBean>> fileBeanMap, String tTime, String[] mintinus) {
        List<FileBean> fileBeans = fileBeanMap.get("minute");
        StringBuffer details = new StringBuffer();
        int actualNum = 0;
        for (int j = 0; j < mintinus.length; j++) {
            boolean rkbs = false;
            String datehour = date + " " + hourList + ":" + mintinus[j];
            if(DateTool.dateStrToTimeStamp(datehour, "yyyy-MM-dd HH:mm") <= (System.currentTimeMillis() + 8 * 60 * 60 *1000)) {
                for (int t = 0; t < asJsonArray.size(); t++) {
                    try {
                        JsonObject asJsonObject = asJsonArray.get(t).getAsJsonObject();
                        JsonObject result = asJsonObject.get("_source").getAsJsonObject();
                        String time = result.get("time").getAsString();
                        //productBean.setTime(time);

                        if (datehour.equals(time)) {
                            rkbs = true;
                            int rateState = param.equals("CO") ? result.get("coRateState").getAsInt() : result.get("idRateState").getAsInt();
                            //String actualNum = param.equals("productCo") ? result.get("coActualNum").getAsString() : result.get("idActualNum").getAsString();
                            productBean.setIdRateState(rateState);
                            //productBean.setIdActualNum(actualNum);
                            buildMcpParam(param, "m", productBean, time, fileBeans, date);
                            actualNum = productBean.getIdActualNum();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        productBean.setDetail(details.toString());
        productBean.setIdActualNum(actualNum);
    }

    public void buildMcpParam(String param, String type, ProductBean productBean, String time, List<FileBean> filelist, String date){
                                                                                                                                                                                     String status = "", idRateState;
        int actualNum = type.equals("m") ? Integer.valueOf(productBean.getIdActualNum()) : 0;
        String qTime = null;
        //全球海表温度去mcp接口查询时，time为前一天
        if("C.0011.0001.R001".equals(productBean.getCts()) && !param.equals("Cre")){
            qTime = DateTool.getNowDateBefore("yyyy-MM-dd HH:mm", Calendar.DATE, -1);
            qTime = qTime.split(" ")[0] + " 00:00";
        }else {
            qTime = time;
        }
        McpQueryBean mcpQueryBean = new McpQueryBean(productBean.getCts(), productBean.getDpc(), productBean.getSod(), qTime);
        JSONArray array;
        if (param.equals("CO")) {
            String IDFileInfo = okHttpTools.query("POST", config.getMcpCoUrl(), JSON.toJSONString(mcpQueryBean, SerializerFeature.DisableCircularReferenceDetect), headers);
            array = JSONObject.parseObject(IDFileInfo).getJSONArray("COFileInfo");
        } else {
            String IDFileInfo = okHttpTools.query("POST", config.getMcpIdUrl(), JSON.toJSONString(mcpQueryBean, SerializerFeature.DisableCircularReferenceDetect), headers);
            array = JSONObject.parseObject(IDFileInfo).getJSONArray("IDFileInfo");
        }

        StringBuffer details = new StringBuffer();
        long timeStamp = DateTool.dateStrToTimeStamp(time, "yyyy-MM-dd HH:mm");
        for (int k = 0; k < filelist.size(); k++) {
            boolean exits = false;

            int i;
            if (type.equals("h")) {
                i = 2;
            }else{
                i = 4;
            }

            //设置应收数actualNum
            if(type.equals("m")){
                //10分钟数据
                String shortFileName = String.format(filelist.get(k).getShortFilename(), time.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", ""));
                if (array == null || array.size() == 0) {
                    if ((System.currentTimeMillis() < (timeStamp + filelist.get(k).getLaterTime()) * 60 * 1000)) {
                        status = "1";
                    }else {
                        //说明缺收
                        status = "2";
                        details.append(String.format(filelist.get(k).getShortFilename(), time.replaceAll("-", "").replaceAll(":", ""))).append("<br/>");
                    }
                    productBean.setStatus(status);
                }else {
                    boolean flag = false;
                    for (int v = 0; v < array.size(); v++) {
                        JSONObject obj = array.getJSONObject(v);
                        if (obj.get("FILE_NAME").toString().endsWith(shortFileName)) {
                            flag = true;
                            actualNum++;
                            break;
                        }
                    }
                    if(flag){
                        //说明已入库的文件和应入库的文件是一致的
                        status = "2".equals(productBean.getStatus()) ? "2" : "1";
                    }else{
                        status = "2";
                        details.append(String.format(filelist.get(k).getShortFilename(), time.replaceAll("-", "").replaceAll(":", ""))).append("<br/>");
                    }
                    productBean.setStatus(status);
                }
            }else{
                productBean.setIdTdNum(filelist.size());
                String tTime = time.replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "");
                String new_tTime = tTime.substring(0, tTime.length() - i);
                String shortFileName = String.format(filelist.get(k).getShortFilename(), new_tTime);

                for (int v = 0; v < array.size(); v++) {
                    JSONObject obj = array.getJSONObject(v);
                    if (obj.get("FILE_NAME").toString().endsWith(shortFileName)) {
                        exits = true;
                        actualNum++;
                        break;
                    }
                }

                if (actualNum == productBean.getIdTdNum()) {
                    status = "1";
                }

                //设置status与detail
                if (!exits) {
                    String hh = DateTool.getSysTime("HH");
                    String t = productBean.getTime();
                    String tmp = t.substring(t.length() - 5, t.length() -3);
                    int count = tmp.equals("08") ? 8 : Integer.parseInt(tmp);
                    if (Integer.parseInt(hh) * 60 < (count * 60 + filelist.get(k).getLaterTime())) status = "0";
                    else {
                        details.append(String.format(filelist.get(k).getShortFilename(), date.replaceAll("-", ""))).append("<br/>");
                        status = "2";
                    }
                }
            }
        }
        productBean.setDetail(details.toString());
        productBean.setIdActualNum(actualNum);
        productBean.setStatus(status);
    }

    public void getHourData(String param, JsonArray asJsonArray, String date, String hourList, ProductBean productBean, Map<String, List<FileBean>> fileBeanMap, String tTime){
        List<FileBean> fileBeans = fileBeanMap.get("hour");
        String datehour = date + " " + hourList;
        boolean rkbs = false;
        for (int t = 0; t < asJsonArray.size(); t++) {
            try {
                JsonObject asJsonObject = asJsonArray.get(t).getAsJsonObject();
                JsonObject result = asJsonObject.get("_source").getAsJsonObject();
                String time = result.get("time").getAsString();
                //productBean.setTime(time);

                if (datehour.equals(time)) {
                    rkbs = true;
                    int idTdNum = param.equals("CO") ? result.get("coTdNum").getAsInt() : result.get("idTdNum").getAsInt();
                    productBean.setIdTdNum(idTdNum);
                    buildMcpParam(param, "h", productBean, time, fileBeans, date);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //这里用来判断当前时刻如果过了超时时间，demo表仍未检索到该时次di，则设置其状态为未到或者异常。例如当前时刻为03：00.查询时次为01时次，则判断当前时刻是否大于 01时次加上超时时间。
        if (!rkbs) {
            String time = DateTool.getSysTime("HH");
            StringBuffer details = new StringBuffer();
            String status = "0";
            for (int k = 0; k < fileBeans.size(); k++) {
                FileBean fileBean = fileBeans.get(k);
                if (Integer.parseInt(time) * 60 < (Integer.parseInt(hourList.substring(0, 2)) * 60 + fileBean.getLaterTime())) {
                    status = "0";
                    details.setLength(0);
                } else {
                    status = "2";
                    details.append(String.format(fileBean.getShortFilename(), (date.replaceAll("-", "") + hourList).substring(0, 10))).append("<br/>");
                }
            }
            productBean.setStatus(status);
            productBean.setDetail(details.toString());
        }
    }

    public void getDayData(String param, String date, String hourList, ProductBean productBean, Map<String, List<FileBean>> fileBeanMap, String tTime){
        List<FileBean> AMlist = fileBeanMap.get("AM");
        List<FileBean> PMlist = fileBeanMap.get("PM");
        String time = date + " " + hourList;
        //productBean.setTime(time);
        if (("08:00".equals(hourList) || "10:00".equals(hourList)) && AMlist != null) {
            buildMcpParam(param, "d", productBean, time, AMlist, date);
        }
        if ("20:00".equals(hourList) && PMlist != null) {
            buildMcpParam(param, "d", productBean, time, PMlist, date);
        }
    }
}
