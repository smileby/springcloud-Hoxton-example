package com.baiyun.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("hello")
public class HelloController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("get")
    public String get(){
        // 在RestTemplate开启LoadBalanced后，不能再直接通过ip：端口的方式进行调用，否则报错： No instances available for 127.0.0.1
//        ResponseEntity<String> entity = restTemplate.getForEntity("http://127.0.0.1:8001/discovery/getName", String.class);
        ResponseEntity<String> entity = restTemplate.getForEntity("http://springcloud-nacos-discovery-provider/discovery/getName", String.class);
        System.out.println(entity.getBody());
        return entity.getBody();
    }
}
