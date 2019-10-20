package cn.uyun.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by 吴晗 on 2017/10/16.
 */
public class HttpUtils {
    public static String success(HashMap data){
        LinkedHashMap map = new LinkedHashMap();
        map.put("success", true);
        map.put("data", data);
        return JSON.toJSONString(map, SerializerFeature.DisableCircularReferenceDetect);
    }

    public static String error(){
        LinkedHashMap map = new LinkedHashMap();
        map.put("success", false);
        map.put("data", "未从缓存中获取到数据！");
        return JSON.toJSONString(map, SerializerFeature.DisableCircularReferenceDetect);
    }
}
