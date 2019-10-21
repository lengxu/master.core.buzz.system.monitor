package cn.uyun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author wuhan
 * @date 2019-07-05
 */
@SpringBootApplication
@EnableScheduling
public class ArtApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArtApplication.class, args);
    }
}
