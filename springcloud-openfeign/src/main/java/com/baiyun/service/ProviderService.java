package com.baiyun.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@Component
@FeignClient("springcloud-nacos-discovery-provider")
@RequestMapping("discovery")
public interface ProviderService {
    @RequestMapping("getName")
    public String getName();
}
