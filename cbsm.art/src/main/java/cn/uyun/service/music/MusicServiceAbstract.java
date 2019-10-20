package cn.uyun.service.music;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author wuhan
 * @date 2019-10-20
 * 该类用来存放两套 环境通用的一些内容
 */

public class MusicServiceAbstract {
    @Autowired
    private MusicService musicService;

    /**
     * 1.1.1 返回数据的字节数
     * 1.1.3 调用次数
     * */
    public Object return_byte_size_query_time() {
        return musicService.return_byte_size_query_time();
    }

    /**
     * 接口调用耗时
     */
    public Object call_spend_second() {
        return musicService.call_spend_second();
    }
}
