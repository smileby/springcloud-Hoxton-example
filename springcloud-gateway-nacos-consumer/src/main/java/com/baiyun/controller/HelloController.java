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
        //
        ResponseEntity<String> entity = restTemplate.getForEntity("http://springcloud-gateway/provider/discovery/getName", String.class);
        System.out.println(entity.getBody());
        return entity.getBody();
    }
}
