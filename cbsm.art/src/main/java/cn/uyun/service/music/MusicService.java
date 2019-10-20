package cn.uyun.service.music;/**
 * @author wuhan
 * @date 2019-10-20
 */

import org.springframework.stereotype.Component;

/**
 *@author wuhan
 *@date 2019-10-20
 */
@Component
public interface MusicService {
    /**
     * 1.1.1 返回数据的字节数
     * 1.1.3 调用次数
     * */
    public Object return_byte_size_query_time();

    /**
     * 接口调用耗时
     */
    public Object call_spend_second();
}
