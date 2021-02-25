## Springcloud gateway
### 原作者地址： https://www.cnblogs.com/crazymakercircle/p/11704077.html
### SpringCloud Gateway简介
> SpringCloud Gateway 作为 Spring Cloud 生态系统中的网关，目标是替代 Zuul，在Spring Cloud 2.0以上版本中，
  没有对新版本的Zuul 2.0以上最新高性能版本进行集成，仍然还是使用的Zuul 2.0之前的非Reactor模式的老版本。
  而为了提升网关的性能，SpringCloud Gateway是基于WebFlux框架实现的，而WebFlux框架底层则使用了高性能的Reactor模式通信框架Netty。

> Spring Cloud Gateway 的目标，不仅提供统一的路由方式，并且基于 Filter 链的方式提供了网关基本的功能，例如：安全，监控/指标，和限流

> Spring Cloud Gateway 底层使用了高性能的通信框架Netty。

### SpringCloud Gateway特征
* 基于 Spring Framework 5，Project Reactor 和 Spring Boot 2.0
* 集成 Hystrix 断路器
* 集成 Spring Cloud DiscoveryClient
* Predicates 和 Filters 作用于特定路由，易于编写的 Predicates 和 Filters
* 具备一些网关的高级功能：动态路由、限流、路径重写
> 以上的特征来说，和Zuul的特征差别不大。SpringCloud Gateway和Zuul主要的区别，还是在底层的通信框架上。
  简单说明一下上文中的三个术语
>> * `Filter（过滤器）`
    和Zuul的过滤器在概念上类似，可以使用它拦截和修改请求，并且对上游的响应，进行二次处理。
    过滤器为org.springframework.cloud.gateway.filter.GatewayFilter类的实例
>> * `Route（路由）`
    网关配置的基本组成模块，和Zuul的路由配置模块类似。一个Route模块由一个 ID，一个目标 URI，一组断言和一组过滤器定义。
    如果断言为真，则路由匹配，目标URI会被访问。
>> * `Predicate（断言）`
    这是一个 Java 8 的 Predicate，可以使用它来匹配来自 HTTP 请求的任何内容，例如 headers 或参数。
    断言的输入类型是一个 ServerWebExchange。

### SpringCloud Gateway和架构
> Spring在2017年下半年迎来了Webflux，Webflux的出现填补了Spring在响应式编程上的空白，Webflux的响应式编程不仅仅是编程风格的改变，
  而且对于一系列的著名框架，都提供了响应式访问的开发包，比如Netty、Redis等等。

> SpringCloud Gateway 使用的Webflux中的reactor-netty响应式编程组件，底层使用了Netty通讯框架。

####  SpringCloud Zuul的IO模型
> Springcloud中所集成的Zuul版本，采用的是Tomcat容器，使用的是传统的Servlet IO处理模型。
  
> 大家知道，servlet由servlet container进行生命周期管理。container启动时构造servlet对象并调用servlet init()进行初始化；
  container关闭时调用servlet destory()销毁servlet；container运行时接受请求，
  并为每个请求分配一个线程（一般从线程池中获取空闲线程）然后调用service()。
  
> `弊端：`servlet是一个简单的网络IO模型，当请求进入servlet container时，servlet container就会为其绑定一个线程，
  在并发不高的场景下这种模型是适用的，但是一旦并发上升，线程数量就会上涨，而线程资源代价是昂贵的（上线文切换，内存消耗大）严重影响请求的处理时间。
  在一些简单的业务场景下，不希望为每个request分配一个线程，只需要1个或几个线程就能应对极大并发的请求，这种业务场景下servlet模型没有优势。

> 所以Springcloud Zuul 是基于servlet之上的一个阻塞式处理模型，即spring实现了处理所有request请求的一个servlet（DispatcherServlet），
  并由该servlet阻塞式处理处理。所以Springcloud Zuul无法摆脱servlet模型的弊端。虽然Zuul 2.0开始，使用了Netty，
  并且已经有了大规模Zuul 2.0集群部署的成熟案例，但是，Springcloud官方已经没有集成改版本的计划

#### Spring Cloud Gateway的处理流程
> 客户端向 Spring Cloud Gateway 发出请求。然后在 Gateway Handler Mapping 中找到与请求相匹配的路由，
  将其发送到 Gateway Web Handler。Handler 再通过指定的过滤器链来将请求发送到我们实际的服务执行业务逻辑，然后返回。


### 路由配置方式
#### 基于配置文件的路由配置方式
> 如果请求的目标地址，是单个的URI资源路径，配置文件示例如下：
```
server:
  port: 8080
spring:
  application:
    name: api-gateway
  cloud:
    gateway:
      routes:
        -id: url-proxy-1
          uri: https://blog.csdn.net
          predicates:
            -Path=/csdn
```
各字段含义如下：
id：我们自定义的路由 ID，保持唯一

uri：目标服务地址

predicates：路由条件，Predicate 接受一个输入参数，返回一个布尔值结果。该接口包含多种默认方法来将 Predicate 组合成其他复杂的逻辑（比如：与，或，非）。

上面这段配置的意思是，配置了一个 id 为 url-proxy-1的URI代理规则，路由的规则为：

当访问地址http://127.0.0.1:8080/csdn/1.jsp时，

会路由到下游地址https://blog.csdn.net/1.jsp。

#### 基于代码的路由配置方式
> 转发功能同样可以通过代码来实现，我们可以在启动类 GateWayApplication 中添加方法 customRouteLocator() 来定制转发规则。
```
package com.springcloud.gateway;
 
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
 
@SpringBootApplication
public class GatewayApplication {
 
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
 
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("path_route", r -> r.path("/csdn")
                        .uri("https://blog.csdn.net"))
                .build();
    }
 
}
```
我们在yml配置文件中注销掉相关路由的配置，重启服务，访问链接：http://localhost:8080/csdn/1.jsp， 可以看到和上面一样的页面，证明我们测试成功。

在实际项目使用中可以将 uri 指向对外提供服务的项目地址，统一对外输出接口。
#### 结合配置中心的路由配置方式
> 在uri的schema协议部分为自定义的lb:类型，表示从微服务注册中心（如Eureka）订阅服务，并且进行服务的路由。
```
server:
  port: 8084
spring:
  cloud:
    gateway:
      routes:
      -id: seckill-provider-route
        uri: lb://seckill-provider
        predicates:
        - Path=/seckill-provider/**

      -id: message-provider-route
        uri: lb://message-provider
        predicates:
        -Path=/message-provider/**

application:
  name: cloud-gateway

eureka:
  instance:
    prefer-ip-address: true
  client:
    service-url:
      defaultZone: http://localhost:8888/eureka/
```
注册中心相结合的路由配置方式，与单个URI的路由配置，区别其实很小，仅仅在于URI的schema协议不同。单个URI的地址的schema协议，一般为http或者https协议。








