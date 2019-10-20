/**
 * Copyright (C), 2018-2019, GTXD有限公司
 * FileName: GxnService
 * Author:   chang
 * Date:     2019/3/18 8:19
 * Description:
 * History:
 * <author>
 * 常路通
 **/
package cn.uyun.service.gxnnodes;

import cn.uyun.entity.config.Config;
import cn.uyun.util.OKHttpTools;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author chang
 * @create 2019/3/18
 * @since 1.0.0
 */
@Service
public class GxnService{
    @Autowired
    private OKHttpTools okHttpTools;

    @Autowired
    private Config config;

    private HashMap headers = new HashMap();

    public Object queryArtNodesCpuAndRam() {
        if(!headers.containsKey("Gridview-Token")) {
            headers.put("Gridview-Token", config.getGvToken());
        }
        //cpu使用率
        String cpu_result = okHttpTools.query(null, config.getGxnMetricUrl() + "CPU Usage", null, headers);
        JSONObject cpu_jsonObject = JSON.parseObject(cpu_result);
        if("0002".equals(cpu_jsonObject.getString("errCode")) && "Not logged in!".equals(cpu_jsonObject.getString("errMsg")) || "0007".equals(cpu_jsonObject.getString("errCode"))){
            String cookie = login();
            headers.put("Cookie", cookie);
            cpu_result = okHttpTools.query(null, config.getGxnMetricUrl() + "CPU Usage", null, headers);
            cpu_jsonObject = JSON.parseObject(cpu_result);
        }
        String cpu_useage = JSON.parseObject(cpu_jsonObject.getJSONObject("resultData").getJSONArray("metric").get(0).toString()).getString("CPU Usage");
        //内存使用率
        String mem_result = okHttpTools.query(null, config.getGxnMetricUrl() + "Memory Usage", null, headers);
        JSONObject mem_jsonObject = JSON.parseObject(mem_result);
        String mem_useage = JSON.parseObject(mem_jsonObject.getJSONObject("resultData").getJSONArray("metric").get(0).toString()).getString("Memory Usage");

        //告警信息
        String result = okHttpTools.query(null, config.getLoginNode(), null, headers);
        JSONObject login_b06 = JSON.parseObject(result).getJSONObject("login_b06");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cpu_useage", cpu_useage);
        jsonObject.put("mem_useage", mem_useage);
        jsonObject.put("alarm_info", login_b06);
        return jsonObject;
    }

    public String login(){
       return okHttpTools.login(null, config.getGxnLogUrl(), null, headers);
    }
}