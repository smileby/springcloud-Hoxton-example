package com.baiyun.route;

import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import reactor.core.publisher.Mono;

/**
 * RouteDefinitionRepository：从存储器中读取路由信息(如内存、配置中心、Redis、MySQL等)
 */
public abstract class AbstractRouteDefinitionRepository implements RouteDefinitionRepository {

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return null;
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return null;
    }
}
