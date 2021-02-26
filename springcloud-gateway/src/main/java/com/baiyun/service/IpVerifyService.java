package com.baiyun.service;


public interface IpVerifyService {

    /**
     * 校验ip是否为有效访问
     * @param ip ip
     * @return Boolean
     */
    Boolean isValid(String ip);
}
