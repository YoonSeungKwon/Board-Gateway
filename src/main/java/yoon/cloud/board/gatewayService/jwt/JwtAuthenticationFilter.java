package yoon.cloud.board.gatewayService.jwt;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    private final JwtUtils jwtUtils;
    public JwtAuthenticationFilter(JwtUtils jwtUtils){
        super(Config.class);
        this.jwtUtils = jwtUtils;
    }
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            if(!request.getHeaders().containsKey("Authorization")){
                return handleUnAuth(exchange);
            }
            String token = request.getHeaders().get("Authorization").get(0);
            if(!token.startsWith("Bearer ")){
                return handleUnAuth(exchange);
            }
            token = token.substring(7);

            if(!jwtUtils.validated(token)){
                return handleUnAuth(exchange);
            }

            return chain.filter(exchange);
        });
    }

    private Mono<Void> handleUnAuth(ServerWebExchange exchange){
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }
    public static class Config{

    }

}
