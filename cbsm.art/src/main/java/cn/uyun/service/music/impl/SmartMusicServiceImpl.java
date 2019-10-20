package cn.uyun.service.music.impl;

import cn.uyun.entity.config.Config;
import cn.uyun.entity.music.PointsQueryBean;
import cn.uyun.service.music.MusicService;
import cn.uyun.util.DateTool;
import cn.uyun.util.Language;
import cn.uyun.util.OKHttpTools;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author wuhan
 * @date 2019-10-20
 */
@Component
@Profile("smart")
public class SmartMusicServiceImpl implements MusicService {
    private static final Logger logger = LoggerFactory.getLogger(SmartMusicServiceImpl.class);
    private static Map<String,String> result_code_message = new HashMap<String,String>();
    private static HashMap<String, String> headers = new HashMap();

    @Autowired
    private OKHttpTools okHttpTools;

    @Autowired
    private Config config;

    public SmartMusicServiceImpl(){
        result_code_message.put("0","查询成功");
        result_code_message.put("-1","查询成功，没有获取到数据（数据库中无数据）");
        result_code_message.put("-1001","用户名缺失");
        result_code_message.put("-1002","用户不正确");
        result_code_message.put("-1003","密码缺失");
        result_code_message.put("-1004","密码错误");
        result_code_message.put("-2001","接口名参数不正确");
        result_code_message.put("-2002","资料代码（单个）参数不正确");
        result_code_message.put("-3001","参数不正确");
        result_code_message.put("-3002","参数缺失");
        result_code_message.put("-3003","禁用的参数");
        result_code_message.put("-4001","没有访问该数据的权限");
        result_code_message.put("-4002","没有访问该资料历史数据的权限");
        result_code_message.put("-4003","访问的时间超出了该用户能访问的最早时间点");
        result_code_message.put("-5001","时间参数跨度超过范围");
        result_code_message.put("-5002","时间参数个数超过限制");
        result_code_message.put("-6001","服务器连接失败");
        result_code_message.put("-7001","SQL错误");
        result_code_message.put("-8001","没有检索到数据");
        result_code_message.put("-8002","文件不存在");
        result_code_message.put("-8003","格点写入数据类型不正确");
        result_code_message.put("-8004","网格部分写入数据数组长度与索引大小不一样");
        result_code_message.put("-8005","网格数据格式实现类初始化失败");
        result_code_message.put("-8006","文件加锁");
        result_code_message.put("-8007","文件不可读");
        result_code_message.put("-8008","文件不可写");
        result_code_message.put("-8009","格点文件读取失败");
        result_code_message.put("-8010","格点文件写入失败");
        result_code_message.put("-8011","多个文件符合条件，不能确定写哪个");
        result_code_message.put("-9001","网格NWP.clit.xml加载失败");
        result_code_message.put("-9002","网格MSG命令解析失败");
        result_code_message.put("-9003","网格套接字读取数据失败");
        result_code_message.put("-9004","网格套接字写入数据失败");
        result_code_message.put("-9005","格点数据数值错误");
        result_code_message.put("-9006","通用接口初始化失败");
        result_code_message.put("-9007","服务端参数处理逻辑失败");
        result_code_message.put("-10001","应用端其他异常");
        result_code_message.put("-10002","服务端其他异常");
        headers.put("Content-Type", "application/json");
    }

    /**
     * 返回数据的字节数
     * 调用次数
     * */
    public Object return_byte_size_query_time() {
        return null;
    }

    /**
     * 1.1.2 接口调用耗时
     * @author HYW
     */
    public Object call_spend_second() {
        String startTime = "";
        String endTime = "";
        List<Object> datas = new ArrayList<Object>();
        Map<String, Object> outMap = new HashMap<String, Object>();
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        PointsQueryBean pqb = new PointsQueryBean();
        Map<String, Object> map = new HashMap<String, Object>();

        long end = System.currentTimeMillis();
        long spend = 6*60*60*1000;
        long interval = 60*1000;
        long start = end - spend;

        if(startTime==null || "".equals(startTime)&&(endTime==null||"".equals(endTime))){
            end = System.currentTimeMillis();
            start = end - spend ;
        }else{
            Date startDate = DateTool.StringToDate(startTime,"yyyy-MM-dd HH:mm:ss");
            Date endDate = DateTool.StringToDate(endTime, "yyyy-MM-dd HH:mm:ss");
            start = startDate.getTime();
            end = endDate.getTime();
        }
        map.put("start", start);
        map.put("end", end);

        map.put("interval", interval);
        map.put("aggregator", "last");
        pqb.setTime(map);

        Map<String,Object> groupBy = new HashMap<String,Object>();
        groupBy.put("tag_keys",new String[]{"sys_user_id","api_data_code","return_code"});
        pqb.setGroup_by(groupBy);

        pqb.setMetric("M.CIMISS.MUSIC.CALL_SPEND_SECOND");
        Map<String, Object> tags = new HashMap<String, Object>();
        //------------NMIC_YJS_GUJUNXIA---SURF_CHN_MUL_HOR------
        tags.put("sys_user_id", "NMIC_YJS_GUJUNXIA");
        tags.put("api_data_code","SURF_CHN_MUL_HOR");
        pqb.setTags(tags);
        if (resultData(tags, pqb)){
            tags.put("sys_user_id", "CMPAS");
            tags.put("api_data_code","地面逐小时");
            list.add(tags);
        }

        //------------NMIC_YJS_GUJUNXIA---SURF_CHN_PRE_MIN------
        tags = new HashMap<String, Object>();
        tags.put("sys_user_id", "NMIC_YJS_GUJUNXIA");
        tags.put("api_data_code","SURF_CHN_PRE_MIN");
        pqb.setTags(tags);
        if (resultData(tags, pqb)){
            tags.put("sys_user_id", "CMPAS");
            tags.put("api_data_code","地面分钟降水");
            list.add(tags);
        }

        //------------NMIC_YJS_GUJUNXIA---RADA_L3_PUP_PRE1H------
        tags = new HashMap<String, Object>();
        tags.put("sys_user_id", "NMIC_YJS_GUJUNXIA");
        tags.put("api_data_code","RADA_L3_PUP_PRE1H");
        pqb.setTags(tags);
        if (resultData(tags, pqb)){
            tags.put("sys_user_id", "CMPAS");
            tags.put("api_data_code","雷达PUP产品");
            list.add(tags);
        }

        //------------NMIC_YJS_GUJUNXIA---RADA_L2_UFMT------
        tags = new HashMap<String, Object>();
        tags.put("sys_user_id", "NMIC_YJS_GUJUNXIA");
        tags.put("api_data_code","RADA_L2_UFMT");
        pqb.setTags(tags);
        if (resultData(tags, pqb)){
            tags.put("sys_user_id", "CMPAS");
            tags.put("api_data_code","雷达基数据");
            list.add(tags);
        }

        //------------NMIC_YJS_ldas---SURF_CHN_MUL_HOR------
        tags = new HashMap<String, Object>();
        tags.put("sys_user_id", "NMIC_YJS_NMIC_YJS_hans");
        tags.put("api_data_code","SURF_CHN_MUL_HOR");
        pqb.setTags(tags);
        if (resultData(tags, pqb)){
            tags.put("sys_user_id", "CLDAS");
            tags.put("api_data_code","地面逐小时");
            list.add(tags);
        }

        //------------NMIC_YJS_03170317---SURF_CHN_MUL_HOR------
        tags = new HashMap<String, Object>();
        tags.put("sys_user_id", "NMIC_YJS_zhuzhi1991");
        tags.put("api_data_code","SURF_CHN_MUL_HOR");
        pqb.setTags(tags);
        if (resultData(tags, pqb)){
            tags.put("sys_user_id", "3DCloudA");
            tags.put("api_data_code","地面逐小时");
            list.add(tags);
        }

        //------------NMIC_YJS_03170317---RADA_L2_UFMT------
        tags = new HashMap<String, Object>();
        tags.put("sys_user_id", "NMIC_YJS_zhuzhi1991");
        tags.put("api_data_code","RADA_L2_UFMT");
        pqb.setTags(tags);
        if (resultData(tags, pqb)){
            tags.put("sys_user_id","3DCloudA");
            tags.put("api_data_code","雷达基数据");
            list.add(tags);
        }

        outMap.put("call_spend_second",list);
        return outMap;
    }

    private boolean resultData(Map<String, Object> outMap, PointsQueryBean pqb) {
        String points = okHttpTools.query("POST", config.getMusicUrl(), JSON.toJSONString(pqb, SerializerFeature.DisableCircularReferenceDetect), headers);
        logger.info("Music接口调用返回数据==============="+points);
        if (points == null || JSONArray.parseArray(points).size()==0) {
            return false;
        } else {
            JSONArray pointArr = JSONArray.parseArray(points);
            List<Map<String,String>> datas = new ArrayList<Map<String, String>>();
            for(int i=0; i<pointArr.size(); i++){
                JSONObject pointObj = (JSONObject) pointArr.get(i);
                JSONObject timeObj = (JSONObject) pointObj.get("points");
                JSONObject groupObj = (JSONObject) pointObj.get("group");
                String result_code = String.valueOf(groupObj.get("return_code"));
                Map parseObject = JSON.parseObject(timeObj.toString(), Map.class);

                Set keySet = parseObject.keySet();
                String s;
                String dateStr;

                for (Object object : keySet){
                    Map<String,String> pointsData = new HashMap<String ,String>();
                    s = object.toString();
                    dateStr = DateTool.dateToString(new Date(Long.parseLong(s)), "yyyy-MM-dd HH:mm:ss");
                    BigDecimal b = new BigDecimal(String.valueOf(parseObject.get(s)));
                    pointsData.put("time",dateStr);
                    pointsData.put("data",b.toBigInteger().toString());
                    pointsData.put("result_code",result_code);
                    pointsData.put("detail",(result_code_message.get(result_code)==null?"":result_code_message.get(result_code)));
                    //  map.put(dateStr, b.toBigInteger().toString());
                    datas.add(pointsData);
                }
            }
            outMap.put("values",datas);
        }

        return true;
    }

    /**
     * 获取时间参数map 用于查询指标
     *
     * @param spend
     * @param interval
     * @return
     */
    private Map<String, Object> getTimePrams(long spend, long interval, String aggregator) {
        Map<String, Object> map = new HashMap<String, Object>();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MILLISECOND, 0);
        long end = c.getTimeInMillis();
        long start = end - spend;
        map.put("start", start);
        map.put("end", end);
        if (aggregator != null) {
            map.put("interval", interval);
            map.put("aggregator", aggregator);
        }
        return map;
    }

    /**
     * 获取music接口的访问字节返回值
     *
     * { "metric": "M.CIMISS.MUSIC.RETURN_BYTE_SIZE", "time": { "start":
     * 1504200011926, "end":1504300111926, "interval":100100, "aggregator":"sum"
     * } }
     */

    public Object getReturnByteSizeReal() {
        PointsQueryBean pqb = new PointsQueryBean();
        Map<String, Object> map = new HashMap<String, Object>();
        long end = System.currentTimeMillis();
        long spend = 300000;
        long start = end - spend;
        map.put("start", start);
        map.put("end", end);

        map.put("interval", spend);
        map.put("aggregator", "sum");
        pqb.setTime(map);

        Map<String, Object> tags = new HashMap<String, Object>();
        tags.put("area_code", "BABJ");
        pqb.setTags(tags);

        pqb.setMetric("M.CIMISS.MUSIC.RETURN_BYTE_SIZE");
        Map<String, Object> group_by = new HashMap<String, Object>();
        String[] strArr = new String[] { "api_data_code","return_code"};
        group_by.put("tag_keys", strArr);
        pqb.setGroup_by(group_by);
        // logger.info("pqb1:"+pqb);
        String result = okHttpTools.query("POTS", config.getMusicUrl(), pqb.toString(), headers);
        // logger.info("result1:"+result);
        JSONArray jsonArr = JSONArray.parseArray(result);
        ArrayList list = new ArrayList();
        outer: for (Object object : jsonArr) {
            JSONObject jo = (JSONObject) object;
            String points = jo.get("points").toString();
            String group = jo.get("group").toString();
            Map pointsMap = JSON.parseObject(points, Map.class);
            Map groupMap = JSON.parseObject(group, Map.class);
            String api_data_code = (String) groupMap.get("api_data_code");
            String return_code = (String)groupMap.get("return_code");
            Collection values = pointsMap.values();
            Object value = null;
            for (Object object1 : values) {
                value = object1;
            }
            if (value != null && 0 != Double.parseDouble(value.toString())) {
                JSONObject jo1 = new JSONObject();
                if (list.size() > 0) {
                    for (Object object2 : list) {
                        JSONObject jsonObj = (JSONObject) object2;
                        if (jsonObj.get("label")
                                .equals(Language.languageTrans(api_data_code, Language.getMusicTransMap()))) {
                            double d = Double.parseDouble(jsonObj.get("value").toString());
                            jsonObj.remove("value");
                            jsonObj.put("value", d + Double.parseDouble(value.toString()));
                            continue outer;
                        }
                    }
                }
                jo1.put("label", Language.languageTrans(api_data_code, Language.getMusicTransMap()));
                jo1.put("value", value);
                list.add(jo1);
            } else {
                continue;
            }
        }
        JSONArray jsonArray = JSONArray.parseArray(list.toString());
        Map<String, Object> outMap = new HashMap<String, Object>();
        outMap.put("result", "success");
        outMap.put("resultData", jsonArray);
        String dateStr = DateTool.dateToString(new Date(System.currentTimeMillis()), "yyyy-MM-dd HH:mm:ss");
        outMap.put("titleTime", dateStr);
        outMap.put("message", "数据加载成功！");
        return outMap;
    }

    /*
     * 获取music接口的访问量(次数)返回值 { "metric": "M.DSXT.SOURCEREC_COUNT2", "time": {
     * "start": 1504200011926, "end":1504300111926, "interval":100100000,
     * //返回数据粒度 "aggregator":"sum" //min/max/avg/sum }, "group_by": {
     * "tag_keys": [ "api_data_code" ] } }
     */
    public Object getVisitNumReal() {
        PointsQueryBean pqb = new PointsQueryBean();
        Map<String, Object> map = new HashMap<String, Object>();
        long end = System.currentTimeMillis();
        long spend = 300000;
        long start = end - spend;
        map.put("start", start);
        map.put("end", end);

        map.put("interval", spend);
        map.put("aggregator", "sum");
        pqb.setTime(map);

        Map<String, Object> tags = new HashMap<String, Object>();
        tags.put("area_code", "BABJ");
        pqb.setTags(tags);

        pqb.setMetric("M.CIMISS.MUSIC.QUERY_TIME");
        Map<String, Object> group_by = new HashMap<String, Object>();
        String[] strArr = new String[] { "api_data_code" };
        group_by.put("tag_keys", strArr);
        pqb.setGroup_by(group_by);
        // logger.info("pqb:"+pqb);
        String result = okHttpTools.query("POTS", config.getMusicUrl(), pqb.toString(), headers);
        // logger.info("result:"+result);
        JSONArray jsonArr = JSONArray.parseArray(result);
        ArrayList list = new ArrayList();
        outer: for (Object object : jsonArr) {
            JSONObject jo = (JSONObject) object;
            String points = jo.get("points").toString();
            String group = jo.get("group").toString();
            Map pointsMap = JSON.parseObject(points, Map.class);
            Map groupMap = JSON.parseObject(group, Map.class);
            String api_data_code = (String) groupMap.get("api_data_code");
            Collection values = pointsMap.values();
            Object value = null;
            for (Object object1 : values) {
                value = object1;
            }

            if (value != null && 0 != Double.parseDouble(value.toString())) {
                JSONObject jo1 = new JSONObject();
                if (list.size() > 0) {
                    for (Object object2 : list) {
                        JSONObject jsonObj = (JSONObject) object2;
                        String str = Language.languageTrans(api_data_code, Language.getMusicTransMap());
                        if (jsonObj.get("label").equals(str)) {
                            double d = Double.parseDouble(jsonObj.get("value").toString());
                            jsonObj.remove("value");
                            jsonObj.put("value", d + Double.parseDouble(value.toString()));
                            continue outer;
                        }
                    }
                }
                jo1.put("label", Language.languageTrans(api_data_code, Language.getMusicTransMap()));
                jo1.put("value", value);
                list.add(jo1);
            } else {
                continue;
            }

        }
        JSONArray jsonArray = JSONArray.parseArray(list.toString());
        Map<String, Object> outMap = new HashMap<String, Object>();
        outMap.put("result", "success");
        outMap.put("resultData", jsonArray);
        String dateStr = DateTool.dateToString(new Date(System.currentTimeMillis()), "yyyy-MM-dd HH:mm:ss");
        outMap.put("titleTime", dateStr);
        outMap.put("message", "数据加载成功！");
        return outMap;
    }
}
