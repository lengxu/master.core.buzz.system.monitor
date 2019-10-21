package cn.uyun.service.product.env.smart;

import cn.uyun.entity.dataProcess.ProductBean;
import cn.uyun.service.product.env.ProductCre;
import com.alibaba.fastjson.JSON;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
@Profile("smart")
public class SmartProductCreImpl implements ProductCre {
    private static final Logger LOGGER = LoggerFactory.getLogger(SmartProductCreImpl.class);

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
    private SimpleDateFormat _dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    //产品生成小时、日数据
    public void getCreDate(JsonArray asJsonArray, String date, String hourAndMinite, ProductBean productBean) {
        //判断是否存在多条DI，如果存在多条，则取receive_time最新的一条
        for (int t = 0; t < asJsonArray.size(); t++) {
            try {
                JsonObject asJsonObject = asJsonArray.get(t).getAsJsonObject();
                JsonObject source = asJsonObject.get("_source").getAsJsonObject();

                //由于天镜全球海表温度资料传递的是世界时，而其他8种资料都是北京时。而大数据则全部都是北京时，因此大数据不用单独调整
                String data_date = source.get("DATA_DATE").getAsString();

                if ((date + " " + hourAndMinite).equals(data_date)) {
                    //新增返回字段新增预报时次
                    productBean.setForcastTime(dateFormat.format(simpleDateFormat.parse(source.get("DATA_DATE").getAsString())));

                    productBean.setIdActualNum(source.get("FILE_NUM").getAsInt());    //FILE_NUM 已生成文件数
                    String state = source.get("DATA_STATE").getAsString();
                    //生成环节es库中的dataStatus字段对应的值为0、1、2.其中0代表正常、1和2代表异常。而前台0代表没数据，1代表绿、2代表红，因此这里得进行转换
                    if (state.equals("2") || state.equals("1")) { //红色
                        productBean.setStatus("2"); //默认为0 灰色, 1绿色，2红色
                        String detail = source.get("FILE_NAME").getAsString();
                        detail = "已生成如下资料：<br/>" + detail.replaceAll(",", "<br/>");
                        productBean.setDetail(detail);
                    } else if (state.equals("0")) { //灰色或者绿色
                        productBean.setStatus("1"); //默认为0 灰色
                    }
                }
            } catch (ParseException e) {
                LOGGER.error("解析DI失败,将跳过该条DI。 失败DI详情：" + JSON.toJSONString(productBean));
                e.printStackTrace();
            }
        }
    }

    //产品生成10分钟数据
    public void getCreMiniteData(JsonArray asJsonArray, String date, ProductBean productBean, String hour, String[] mintinus) {
        int num = 0;
        for (int k = 0; k < mintinus.length; k++) {
            for (int t = 0; t < asJsonArray.size(); t++) {
                try {
                    JsonObject asJsonObject = asJsonArray.get(t).getAsJsonObject();
                    JsonObject source = asJsonObject.get("_source").getAsJsonObject();

                    //由于全球海表温度资料传递的是世界时，而其他8种资料都是北京时，这里针对全球海表温度进行调整，将其调整为世界时
                    String data_date = source.get("DATA_DATE").getAsString();

                    if ((date + " " + hour + ":" + mintinus[k] + ":00").equals(data_date)) {
                        //新增返回字段新增预报时次
                        productBean.setForcastTime(dateFormat.format(simpleDateFormat.parse(source.get("DATA_DATE").getAsString())));
                        num += source.get("FILE_NUM").getAsInt();    //FILE_NUM 已生成文件数
                        String state = source.get("DATA_STATE").getAsString();
                        //生成环节es库中的dataStatus字段对应的值为0、1、2.其中0代表正常、1和2代表异常。而前台0代表没数据，1代表绿、2代表红，因此这里得进行转换
                        if (state.equals("2") || state.equals("1")) { //红色
                            productBean.setStatus("2"); //默认为0 灰色, 1绿色，2红色
                            String detail = source.get("FILE_NAME").getAsString();
                            detail = "已生成如下资料：<br/>" + detail.replaceAll(",", "<br/>");
                            productBean.setDetail(detail);
                        } else if (state.equals("0")) { //灰色或者绿色
                            productBean.setStatus("1"); //默认为0 灰色
                        }
                    }
                } catch (ParseException e) {
                    LOGGER.error("解析DI失败,将跳过该条DI。 失败DI详情：" + JSON.toJSONString(productBean));
                    e.printStackTrace();
                }
            }
            //productBean.setDate(date + " " + hour + ":" + mintinus[k]);
        }
        productBean.setIdActualNum(num);
    }
}
