package com.baiyun.filter.global;


import com.baiyun.service.IpVerifyService;
import com.baiyun.utils.ObjectMapperUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

import static com.baiyun.constants.GatewayConstants.IP_ADDRESS;

/**
 * 实现GlobalFilter接口，自定义
 *      全局过滤器，拦截黑名单IP
 */
@Component
@Slf4j
public class IpFilter implements GlobalFilter, Ordered {

    private final IpVerifyService ipVerifyService;

    public IpFilter(IpVerifyService ipVerifyService) {
        this.ipVerifyService = ipVerifyService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        String ipAddress = getIpAddress(headers, request);
        // TODO 验证逻辑
        Boolean valid = ipVerifyService.isValid(ipAddress);
        if(valid){
            log.info("拦截IP ：[{}]，校验有效",ipAddress);
            exchange.getAttributes().put(IP_ADDRESS,ipAddress);
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {

            }));
        }else{
            log.info("拦截IP ：[{}]，校验无效",ipAddress);
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.FORBIDDEN);
            response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
            String responseValue = "IP校验不通过。";
            byte[] bytes = ObjectMapperUtil.obj2Bytes(responseValue);
            DataBuffer buffer = response.bufferFactory().wrap(bytes);
            return response.writeWith(Flux.just(buffer));
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private String getIpAddress(HttpHeaders headers, ServerHttpRequest request){
          String ip = headers.getFirst("x-forwarded-for");
          if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
              ip = headers.getFirst("Proxy-Client-IP");
          }
          if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
              ip = headers.getFirst("WL-Proxy-Client-IP");
          }
          if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
              ip = headers.getFirst("HTTP_CLIENT_IP");
          }
          if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
              ip = headers.getFirst("HTTP_X_FORWARDED_FOR");
          }
          if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
              ip = Objects.requireNonNull(request.getRemoteAddress()).getHostString();
              if (ip.equals("127.0.0.1")) {
                  // 根据网卡取本机配置的IP
                  InetAddress inetAddress = null;
                  try {
                      inetAddress = InetAddress.getLocalHost();
                      ip = inetAddress.getHostAddress();
                  } catch (UnknownHostException e) {
                      log.info("读取本机IP地址失败。");
                  }
              }
          }
           //对于通过多个代理的情况， 第一个IP为客户端真实IP,多个IP按照','分割 "***.***.***.***".length() =15
          if (ip != null && ip.length() > 15) {
              if (ip.indexOf(",") > 0) {
                  ip = ip.substring(0, ip.indexOf(","));
              }
          }
          return ip;
      }
}
