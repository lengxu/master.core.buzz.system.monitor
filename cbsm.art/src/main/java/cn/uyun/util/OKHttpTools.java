package cn.uyun.util;

import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by 吴晗 on 2017/9/21.
 */
@Component
public class OKHttpTools {

    private static final Logger LOGGER = LoggerFactory.getLogger(OKHttpTools.class);
    private OkHttpClient.Builder builder = new OkHttpClient().newBuilder().readTimeout(10, TimeUnit.SECONDS);

    public Response execute(String type, String url, String data, HashMap headers){
        try{
            OkHttpClient okHttpClient = null;
            Request.Builder requestBuilder = new Request.Builder();
            Request request = null;

            if(headers != null){
                Set<String> set = headers.keySet();
                for (String key : set) {
                    String val = headers.get(key).toString();
                    requestBuilder.addHeader(key, val);
                }
            }
            if(type == null || "GET".equals(type.toUpperCase())){
                request = requestBuilder.url(url).build();
                okHttpClient = this.builder.build();
            }else if("POST".equals(type.toUpperCase())){
                RequestBody requestBody = RequestBody.create(MediaType.parse(String.valueOf(headers.get("Content-Type"))),data.getBytes("utf8"));
                request = requestBuilder.post(requestBody).url(url).build();
                okHttpClient = this.builder.build();
            }
            Response response = okHttpClient.newCall(request).execute();
            return response;
        }catch (IOException e){
            LOGGER.error("调用接口："+url+" 出现异常！",e);
        }
        return null;
    }

    public String login(String type, String url, String data, HashMap headers){
        Response response = execute(type, url, data, headers);
        String cookie = response.header("Set-Cookie");
        return cookie;
    }

    public String query(String type, String url, String data, HashMap headers){
        Response response = execute(type, url, data, headers);
        String result = null;
        if(response.code() == 200){
            try {
                result = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            LOGGER.error("调用接口："+url+" 出现异常！状态码为："+response.code());
        }
        if(response != null){
            response.close();
        }
        return result;
    }
}


