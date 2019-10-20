package cn.uyun.entity.dataProcess;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * @author wuhan
 * @date 2019-06-28
 * 实况分析数据全流程主页数据
 */
@Getter
@Setter
@Component
public class CurrentData {
    private String api_data_code;

    private String forcastTime;

    private String music;

    private String name;

    private String nowStatus;
}
