package com.msa.cloudapigateway.Filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Component
@Slf4j
public class LoggingFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {
    public LoggingFilter(){super(GlobalFilter.Config.class);}

    @Override
    public GatewayFilter apply(GlobalFilter.Config config){
        return new OrderedGatewayFilter((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            log.info(request.getRemoteAddress().getAddress().getHostAddress());
            log.info("로깅 필터 baseMessage: {}",config.getBaseMessage());
            if(config.isPreLogger()){
                log.info("로깅 pre 필터 : Request id -> {}",request.getId());
            }
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                if(config.isPostLogger()){
                    log.info("로깅 post 필터 : response code -> {}",response.getStatusCode());
                }
            }));
        },Ordered.LOWEST_PRECEDENCE);

    }
    @Data
    public static class Config{
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
}
