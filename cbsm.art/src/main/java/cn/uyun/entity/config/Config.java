package cn.uyun.entity.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by 吴晗 on 2017/10/12.
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "config")
public class Config {
    private String ftpServer;

    private String cldasUser;

    private String cldasPwd;

    private String cloudUser;

    private String cloudPwd;

    private String cloudRealtimeUser;

    private String cloudRealtimePwd;

    private String alertUrl;

    private String delFrequency;

    private String type;

    private String system;

    private String groupId;

    private String msgType;

    private String colType;

    private String dataForm;

    private String criticalEvnetType;

    private String infoEvnetType;

    private String eventLevel;

    private String eventTitle;

    private String kEvent;

    private String eventTrag;

    private String waitEventSuggest;

    private String eventSuggest;

    private String cloudDownload;

    private String cloudPrepare;

    private String cloudProcess;

    private String cloudPostprocess;

    private String cloudRealtimeDownload;

    private String cloudRealtimePrepare;

    private String cloudRealtimeProcess;

    private String CloudRealtimePostprocess;

    //高性能登陆节点
    //gxn-log-url
    private String gxnLogUrl;

    //gxn-metric-url
    private String gxnMetricUrl;

    //gv-token
    private String gvToken;

    //login-node
    private String loginNode;

    //music-url
    private String musicUrl;

    private String mcpCoUrl;

    private String mcpIdUrl;
}
