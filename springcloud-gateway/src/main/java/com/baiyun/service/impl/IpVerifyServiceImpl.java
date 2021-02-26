package com.baiyun.service.impl;


import com.baiyun.service.IpVerifyService;
import org.springframework.stereotype.Service;


@Service
public class IpVerifyServiceImpl implements IpVerifyService {

    @Override
    public Boolean isValid(String ip) {
        return true;
    }
}
