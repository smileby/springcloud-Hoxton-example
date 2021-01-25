package com.baiyun;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * feign注解不加的话，应用会启动失败
 *
 * 访问 http://127.0.0.1:8003/feign/hello
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class OpenfeignApp {
    public static void main(String[] args) {
        SpringApplication.run(OpenfeignApp.class);
    }
}
