package cn.uyun.controller;

import cn.uyun.service.gxnLog.GxnLogStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * Created by WuHan on 2018/07/24.
 */
@RestController
public class ZNWGController {

    private static final Logger logger = LoggerFactory.getLogger(ZNWGController.class);

    @Autowired
    private GxnLogStatusService gxnLogStatusService;

    @RequestMapping(value = "/art/api/gxnlog/status/query", method = RequestMethod.GET)
    public String queryZnwgData(){
        ArrayList<String> modelNames = new ArrayList<String>();
        modelNames.add("3D_Cloud_5km");
        modelNames.add("3D_Cloud_5km_3D");
        modelNames.add("CLDAS_CHN_5KM_HOR_6W");
        modelNames.add("CMPAS_CHN_5KM_HOR_FAST");
        modelNames.add("CMPAS_CHN_5KM_HOR_FRT");
        modelNames.add("CODAS_GLB_DAY");
        modelNames.add("CODAS_SST_GLB_DAY");
        return gxnLogStatusService.queryStatus(modelNames);
    }
}
