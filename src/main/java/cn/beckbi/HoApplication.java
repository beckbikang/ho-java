package cn.beckbi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @program: ho-java
 * @description:
 * @author: bikang
 * @create: 2022-10-09 22:00
 */
@SpringBootApplication
@EnableDiscoveryClient
public class HoApplication {
    public static void main(String[] args) {
        SpringApplication.run(HoApplication.class, args);
    }
}
