package cn.uyun.controller;

import cn.uyun.service.music.MusicService;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by lihonghua on 2018/5/22.
 */
@RestController
@RequestMapping(value = "/art/api")
public class MusicController {

    private static final Logger logger = LoggerFactory.getLogger(MusicController.class);

    @Autowired
    private MusicService musicService;

    //music接口数据获取监视
    @RequestMapping(value = "/callspendsecond/query" , method = RequestMethod.POST)
    public String query_call_spend_second(){
         String result = JSONObject.toJSONString(musicService.call_spend_second());
        return result;
    }

    /**
     * 产品访问情况
     * 调用/store/openapi/v2/datapoints/query?apikey=接口，查询当前时刻往前推6小时至当前时刻的数据，汇聚粒度为1小时，求和
     */
    @RequestMapping("/timeandreturnbytesize/query")
    public String query_query_time_and_return_byte_size(){
        String result = JSONObject.toJSONString(musicService.return_byte_size_query_time());
        return result;
    }

}
