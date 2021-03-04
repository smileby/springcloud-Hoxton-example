package com.baiyun.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("discovery")
public class ProviderController {

    @RequestMapping("getName")
    public String getName(){
//        throw new RuntimeException("provider 自定义异常！");
        return "springcloud-gateway-nacos-provider";
    }
}
