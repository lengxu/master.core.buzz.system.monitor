package cn.uyun;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.stereotype.Component;

/**
 * @author wuhan
 * @date 2019-10-15
 */
@Component
public class WebServer implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebServer.class);

    public void customize(ConfigurableServletWebServerFactory factory) {
        String start_port = System.getProperty("START_PORT");
        if (StringUtils.isNotBlank(start_port)) {
            try {
                int port = Integer.parseInt(start_port);
                factory.setPort(port);
            } catch (Exception e) {
                LOGGER.error("参数START_PORT非法，请检查启动脚本START_PORT的值！");
                e.printStackTrace();
            }
        } else {
//            LOGGER.error("启动脚本中未指定启动端口START_PORT参数，异常退出！");
//            System.exit(-1);
            LOGGER.error("启动脚本中未指定启动端口START_PORT参数，将使用预置端口30018！");
            factory.setPort(30018);
        }
    }
}
