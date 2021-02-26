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
    过滤器为org.springframework.cloud.gateway.filter.GatewayFilter类的实例。 
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
#### 基于配置文件的路由配置方式1
> 如果请求的目标地址，是单个的URI资源路径，配置文件示例如下：
```
server:
  port: 8006
spring:
  application:
    name: springcloud-gateway
  cloud:
    gateway:
      routes: # HelloController请求http://springcloud-gateway/provider/discovery/getName
        - id: provider_route
          uri: lb://springcloud-gateway-nacos-provider
          predicates:
            - Path=/provider/** #访问地址
          filters:
            - StripPrefix=1
```
各字段含义如下：
id：我们自定义的路由 ID，保持唯一

uri：目标服务地址

predicates：路由条件，Predicate 接受一个输入参数，返回一个布尔值结果。该接口包含多种默认方法来将 Predicate 组合成其他复杂的逻辑（比如：与，或，非）。

上面这段配置的意思是，配置了一个 id 为 provider_route的URI代理规则，路由的规则为：

当访问地址http://springcloud-gateway/provider/discovery/getName时，

会路由到下游地址 http://127.0.0.1:8007/discovery/getName
#### 基于配置文件的路由配置方式2
```
server:
  port: 8006
spring:
  application:
    name: springcloud-gateway
  cloud:
      gateway:
        discovery:
          locator:
            enabled: true
            lower-case-service-id: true
```
各字段含义如下：

enabled：表明gateway开启服务注册和发现的功能,，
并且spring cloud gateway自动根据服务发现为每一个服务创建了一个router
这个router将以服务名开头的请求路径转发到对应的服务

lower-case-service-id：将请求路径上的服务名配置为小写
（因为服务注册的时候，向注册中心注册时将服务名转成大写的了【Eureka会转大写，Nacos不会】），
比如以/service-hi/*的请求路径被路由转发到服务名为service-hi的服务上。

当访问地址 http://springcloud-gateway/springcloud-gateway-nacos-provider/discovery/getName时，

会路由到下游地址 http://127.0.0.1:8007/discovery/getName

#### 基于代码的路由配置方式
> 转发功能同样可以通过代码来实现，我们可以在启动类 GateWayApplication 中添加方法 customRouteLocator() 来定制转发规则。
```
package com.baiyun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class GatewayApp {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApp.class);
    }

    /**
     * 配置路由
     * @param builder
     * @return
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("provider_route", r -> r.path("/provider/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://springcloud-gateway-nacos-provider"))
                .build();
    }

}

```
我们在yml配置文件中注销掉相关路由的配置，重启服务，访问链接：http://springcloud-gateway/provider/discovery/getName时，可以看到正常的返回，证明我们测试成功。
在实际项目使用中可以将 uri 指向对外提供服务的项目地址，统一对外输出接口。

当访问地址http://springcloud-gateway/provider/discovery/getName时，

会路由到下游地址 http://127.0.0.1:8007/discovery/getName

#### 结合配置中心的路由配置方式
> 在uri的schema协议部分为自定义的lb:类型，表示从微服务注册中心（如Eureka、nacos）订阅服务，并且进行服务的路由。
```
server:
  port: 8006
spring:
  application:
    name: springcloud-gateway
  cloud:
    gateway:
      enabled: true
      #discovery:
        #locator:
          # 表明gateway开启服务注册和发现的功能,，并且spring cloud gateway自动根据服务发现为每一个服务创建了一个router
          # 这个router将以服务名开头的请求路径转发到对应的服务
          #enabled: true
          # 将请求路径上的服务名配置为小写（因为服务注册的时候，向注册中心注册时将服务名转成大写的了），比如以/service-hi/*的请求路径被路由转发到服务名为service-hi的服务上。
          #lower-case-service-id: true
      routes: 
        - id: provider_route
          uri: lb://springcloud-gateway-nacos-provider
          predicates:
            - Path=/provider/** 
          filters:
            - StripPrefix=1
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
        enabled: true
management:
  endpoints:
    web:
      exposure:
        include: '*'
  endpoint:
    health:
      show-details: always
logging:
  level:
    org.springframework: debug


```
注册中心相结合的路由配置方式，与单个URI的路由配置，区别其实很小，仅仅在于URI的schema协议不同。单个URI的地址的schema协议，一般为http或者https协议。


#### 动态路由
待完善

### 过滤器Filter
> 网关经常需要对路由请求进行过滤，如鉴权之后构造头部之类的，过滤的种类很多，如增加请求头、增加请求参数 、增加响应头和断路器等等功能，
这就用到了Spring Cloud Gateway 的 Filter。

#### Filter作用
> * 当我们有很多个服务时，客户端请求各个服务的Api时，每个服务都需要做相同的事情，比如鉴权、限流、日志输出等

> * 因此可以将这些功能等转移到网关处理以减少重复开发。
#### Filter 的生命周期
> * `PRE：`这种过滤器在被代理的微服务（Proxied Service）执行之前调用。我们可利用这种过滤器实现身份验证、在集群中选择请求的微服务、记录调试信息等。

> * `POST：`这种过滤器在被代理的微服务（Proxied Service）执行完成后执行。这种过滤器可用来为响应添加标准的 HTTP Header、收集统计信息和指标、将响应从微服务发送给客户端等。
#### Filter 的分类
> * GatewayFilter：应用到单个路由或者一个分组的路由上。自定义的GatewayFilter，同样需要指定某个路由器

> * GlobalFilter：应用到所有的路由上。当请求到来时，`Filtering Web Handler` 处理器会添加所有 `GlobalFilter` 实例和匹配的 `GatewayFilter` 实例到过滤器链中。因此，自定义的GlobalFilter，不需要什么额外的配置。

> * 自定义 GatewayFilter，有两种实现方式

>> 一种是直接 实现GatewayFilter接口，不过还需要手动将自定义的GatewayFilter 注册到 router中

>> 另一种是 自定义过滤器工厂（继承AbstractGatewayFilterFactory类） , 选择自定义过滤器工厂的方式，
可以在配置文件中配置过滤器