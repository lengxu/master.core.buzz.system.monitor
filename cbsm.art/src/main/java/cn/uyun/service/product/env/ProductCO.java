package cn.uyun.service.product.env;/**
 * @author wuhan
 * @date 2019-10-21
 */

import cn.uyun.entity.dataProcess.FileBean;
import cn.uyun.entity.dataProcess.ProductBean;
import com.google.gson.JsonArray;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 *@author wuhan
 *@date 2019-10-21
 */
@Component
public interface ProductCO {
    public void getMiniteData(String param, JsonArray asJsonArray, String date, String hourList, ProductBean productBean, Map<String, List<FileBean>> fileBeanMap, String tTime, String[] mintinus);

    public void getHourData(String param, JsonArray asJsonArray, String date, String hourList, ProductBean productBean, Map<String, List<FileBean>> fileBeanMap, String tTime);

    public void getDayData(String param, JsonArray asJsonArray, String date, String hourList, ProductBean productBean, Map<String, List<FileBean>> fileBeanMap, String tTime);
}
