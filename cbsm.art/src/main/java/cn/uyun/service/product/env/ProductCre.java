package cn.uyun.service.product.env;/**
 * @author wuhan
 * @date 2019-10-21
 */

import cn.uyun.entity.dataProcess.ProductBean;
import com.google.gson.JsonArray;
import org.springframework.stereotype.Component;

/**
 *@author wuhan
 *@date 2019-10-21
 */
@Component
public interface ProductCre {
    public void getCreDate(JsonArray asJsonArray, String date, String hourAndMinite, ProductBean productBean);

    public void getCreMiniteData(JsonArray asJsonArray, String date, ProductBean productBean, String hour, String[] mintinus);
}
