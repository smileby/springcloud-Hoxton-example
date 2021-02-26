package com.baiyun.service.impl;

import com.baiyun.service.PathVerifyService;
import org.springframework.stereotype.Service;


@Service
public class PathVerifyServiceImpl implements PathVerifyService {

    @Override
    public Boolean shouldFilter(String path) {
        return true;
    }
}
