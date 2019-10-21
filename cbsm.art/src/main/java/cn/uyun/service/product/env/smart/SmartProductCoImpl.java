package cn.uyun.service.product.env.smart;

import cn.uyun.entity.config.Config;
import cn.uyun.entity.dataProcess.FileBean;
import cn.uyun.entity.dataProcess.McpQueryBean;
import cn.uyun.entity.dataProcess.ProductBean;
import cn.uyun.service.product.env.ProductCO;
import cn.uyun.util.DateTool;
import cn.uyun.util.OKHttpTools;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @author wuhan
 * @date 2019-10-21
 */
@Component
@Profile("smart")
public class SmartProductCoImpl implements ProductCO {
    @Autowired
    private OKHttpTools okHttpTools;

    @Autowired
    private Config config;

    //收集与入库10分钟数据
    public void getMiniteData(String param, JsonArray asJsonArray, String date, String hourList, ProductBean productBean, Map<String, List<FileBean>> fileBeanMap, String tTime, String[] mintinus) {
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
                        String time = result.get("DATA_TIME").getAsString();
                        //productBean.setTime(time);

                        if (datehour.equals(time)) {
                            rkbs = true;
                            int rateState = param.equals("CO") ? result.get("CO_TD").getAsInt() : result.get("ID_TD_"+config.getMcpDestination()).getAsInt();
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

    public void buildMcpParam(String param, String type,ProductBean productBean, String time, List<FileBean> filelist, String date){
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
        McpQueryBean mcpQueryBean = new McpQueryBean(productBean.getCts(), productBean.getDpc(), productBean.getSod(), qTime, config.getMcpDestination());
        JSONArray array;
        if (param.equals("CO")) {
            String IDFileInfo = okHttpTools.query("POST", config.getMcpCoUrl(), JSON.toJSONString(mcpQueryBean, SerializerFeature.DisableCircularReferenceDetect), null);
            array = JSONObject.parseObject(IDFileInfo).getJSONArray("COFileInfo");
        } else {
            String IDFileInfo = okHttpTools.query("POST", config.getMcpIdUrl(), JSON.toJSONString(mcpQueryBean, SerializerFeature.DisableCircularReferenceDetect), null);
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
                    String tmp = t.split(":")[0];
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
                String time = result.get("DATA_TIME").getAsString();
                //productBean.setTime(time);

                if (datehour.equals(time)) {
                    rkbs = true;
                    int idTdNum = param.equals("CO") ? result.get("CO_TD").getAsInt() : result.get("ID_TD_"+config.getMcpDestination()).getAsInt();
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

    public void getDayData(String param, JsonArray asJsonArray, String date, String hourList, ProductBean productBean, Map<String, List<FileBean>> fileBeanMap, String tTime){
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
