package com.baiyun.controller;

import com.baiyun.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("feign")
@RestController
public class OpenfeignController {

    @Autowired
    private ProviderService providerService;


    @RequestMapping("hello")
    public String hello(){
        String name = providerService.getName();
        System.out.println(name);
        return name;
    }
}
