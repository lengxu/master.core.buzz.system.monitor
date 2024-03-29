package cn.uyun.controller;

import cn.uyun.service.product.ProductServiceAbs;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * Created by lihonghua on 2018/5/28.
 */
@RestController
@RequestMapping(value = "/art/api")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @RequestMapping("/{dataProcess}/query")
    public String queryDataProcess(@PathVariable String dataProcess) {
        String param = "";
        if ("productCre".equals(dataProcess)) {
            param = "Cre";
        } else if ("productCo".equals(dataProcess)) {
            param = "CO";
        } else if ("produc".equals(dataProcess)) {
            param = "DI";
        } else {
            logger.error("调用数据全流程详情接口时，参数有误！");
        }
        return JSON.toJSONString(ProductServiceAbs.detailMap.get(param), SerializerFeature.DisableCircularReferenceDetect);
    }

    @RequestMapping("/productNewAndHistory/query")
    public String queryProductNewAndHistory() {
        HashMap map = ProductServiceAbs.currentDataMap;
        return JSON.toJSONString(map, SerializerFeature.DisableCircularReferenceDetect);
    }
}
