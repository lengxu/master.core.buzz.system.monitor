/**
 * Copyright (C), 2018-2019, GTXD有限公司
 * FileName: GxnController
 * Author:   chang
 * Date:     2019/3/18 8:12
 * Description:
 * History:
 * <author>
 * 常路通
 **/
package cn.uyun.controller;

import cn.uyun.service.gxnnodes.GxnService;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author chang
 * @create 2019/3/18
 * @since 1.0.0
 */
@RestController
public class GxnController {
    private static final Logger logger = LoggerFactory.getLogger(GxnController.class);
    @Autowired
    private GxnService gxnService;
    @RequestMapping("/art/api/artNodesCpuAndRam/query")
    public String queryArtNodesCpuAndRam(){
        String result = JSONObject.toJSONString(gxnService.queryArtNodesCpuAndRam());
        return result;
    }
}