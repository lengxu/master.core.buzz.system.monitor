package cn.info.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

/**
 * @author wuhan
 * @date 2019-09-24
 */
@RestController
@RequestMapping(value = "/api/v1/speed")
public class GetWay {
    private static Random random = new Random();

    @RequestMapping(value = "/througput")
    public Object testThrougput(){
        long sTime = System.currentTimeMillis();

        long eTime = System.currentTimeMillis();
        int i = 0;
        synchronized (this) {
            i = random.nextInt(10000);
        }
        return i;
    }
}
