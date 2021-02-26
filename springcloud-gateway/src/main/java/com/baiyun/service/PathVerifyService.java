package com.baiyun.service;


public interface PathVerifyService {

    /**
     * 判断路径是否需要鉴权
     * @param path
     * @return
     */
    Boolean shouldFilter(String path);

}
