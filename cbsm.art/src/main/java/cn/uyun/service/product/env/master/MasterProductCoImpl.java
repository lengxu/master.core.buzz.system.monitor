package cn.uyun.service.product.env.master;

import cn.uyun.dao.es.EsOperation;
import cn.uyun.entity.config.Config;
import cn.uyun.entity.dataProcess.FileBean;
import cn.uyun.entity.dataProcess.ProductBean;
import cn.uyun.service.product.env.ProductCO;
import cn.uyun.util.OKHttpTools;
import com.google.gson.JsonArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author wuhan
 * @date 2019-10-21
 */
@Component
@Profile("master")
public class MasterProductCoImpl implements ProductCO {
    @Autowired
    private OKHttpTools okHttpTools;

    @Autowired
    private Config config;

    @Autowired
    protected EsOperation esOperation;

    //收集与入库10分钟数据
    public void getMiniteData(String param, JsonArray asJsonArray, String date, String hourList, ProductBean productBean, Map<String, List<FileBean>> fileBeanMap, String tTime, String[] mintinus) {

    }

    public void buildMcpParam(String param, String type, ProductBean productBean, String time, List<FileBean> filelist, String date){

    }

    public void getHourData(String param, JsonArray asJsonArray, String date, String hourList, ProductBean productBean, Map<String, List<FileBean>> fileBeanMap, String tTime){

    }

    public void getDayData(String param, JsonArray asJsonArray, String date, String hourList, ProductBean productBean, Map<String, List<FileBean>> fileBeanMap, String tTime){

    }
}
