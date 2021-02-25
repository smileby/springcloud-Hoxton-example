# springcloud-Hoxton-example
 * SpringCloud 版本对应： Hoxton
 * 注册中心使用Nacos、Consul
 
>  配置文件优先级（由高到低）：bootstrap.properties > bootstrap.yml > application.properties > application.yml
 

 
> * ========================================
> * springcloud-consul                      ===》
> * springcloud-loadbalancer                ===》
> * springcloud-openfeign                   ===》
> * springcloud-sentienl                    ===》
> * springcloud-gateway                     ===》

> * springcloud-alibaba-nacos-discovery     ===》
> * springcloud-alibaba-nacos-config        ===》
> * ========================================

* 组件替代方案
> 服务注册中心-Eureka
>> * 官方停止更新，并且已经有更好的替代产品了，可以使用，但是官方已经不建议使用了（重度患者）。
>> * Zookeeper：某些老系统，以前是用的Zookeeper + Dubbo，后来做技术升级，结果发现SpringCloud的Eureka停更了，然后就用了最少的技术切换，那么就用了Zookeeper做注册中心
>> * Consul：go语言开发的，也是一个优秀的服务注册框架，但是使用量较少，风头都被Nacos抢了。
>> * Nacos：来自于SpringCloudAlibaba，在企业中经过了百万级注册考验的，不但可以完美替换Eureka，还能做其他组件的替换，所以强烈建议使用，是学习的重点。

> 服务调用-Ribbon
>> * Ribbon：也进入了维护状态，停止更新了，但是Spring官方还在使用（轻度患者）。
>> * LoadBalancer：Spring官方推出的一个新的组件，打算逐渐取代掉Ribbon，但是现在还处于萌芽状态。

> 服务调用-Feign
>> * Netflix 公司产品，也停止更新了。
>> * OpenFeign: Spring社区等不了Netflix更新了，然后就自己做了一个组件，`OpenFeign`。

> 服务降级-Hystrix
>> * Hystrix：官网不推荐使用，但是中国企业中还在大规模使用。
>> * Resilience4J：官网推荐使用，但是国内很少用这个。
>> * Sentienl：来自于SpringCloudAlibaba，在中国企业替换Hystrix的组件，国内强烈建议使用。

> 服务网关-zuul
>> * Zuul：Netflix 公司产品，公司内部产生分歧，有的人想自己出一个Zuul2。
>> * Zuul2：也是Netflix 公司准备出的产品，但是由于内部分歧，所以Zuul2已经胎死腹中了。
>> * gateway：Spring社区自己出的网关组件，官方隆重介绍和极度推荐的网关服务组件

> 服务配置-config
>> * Config：目前也在使用，风头被Nacos抢了。
>> * Nacos：来自于SpringCloudAlibaba，后来居上，把Config给替换了。

> 服务总线：
>> * Bus：SpringCloud原生的服务总线组件，现在风头也被Nacos抢了。
>> * Nacos：来自于SpringCloudAlibaba，后来居上，把Bus给替换了。

* nacos-server从github下载 nacos-server-2.0.0-ALPHA.1.zip，windows环境点击startup.cmd启动
* 访问127.0.0.1:8848/nacos/index.html

| 应用 | 描述 | 
| :--- | :--- |
| springcloud-nacos-discovery-provider | 基于nacos的服务注册 | 
| springcloud-nacos-discovery-consumer | 基于nacos的服务发现 |
| springcloud-nacos-config | 基于nacos的配置中心 |
| springcloud-openfeign | OpenFeign调用组件 |
| springcloud-websocket | springboot集成websocket |