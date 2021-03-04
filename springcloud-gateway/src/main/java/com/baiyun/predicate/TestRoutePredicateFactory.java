package com.baiyun.predicate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;


/**
 * 自定义路由匹配规则【日期路由】
 * 类命名一定要以RoutePredicateFactory结尾
 *
 * 参考： https://www.cnblogs.com/wangjunwei/p/12898696.html
 *
 */
@Slf4j
@Component
public class TestRoutePredicateFactory extends AbstractRoutePredicateFactory<TestRoutePredicateFactory.Config> {

    public static final String DATETIME_KEY = "datetime";

    /**
     * 配置信息传入
     */
    public TestRoutePredicateFactory() {
        super(Config.class);
    }

    /**
     * 参数列表与Config属性之间的映射
     * @return
     */
    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.singletonList(DATETIME_KEY);
    }

    /**
     * 属性值的处理
     * @param config
     * @return
     */
    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        String datetime = config.getDatetime();
//        log.info("datetime={}",datetime);
        return exchange -> true;
    }

    /**
     * @Description: 用于承载Predicate所需的参数
     * @Param: 这里是判断输入的时间与真实时间之间的先后关系
     */
    public static class Config {
        private String datetime;

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }
    }
}
