package com.wolroys.shopgateway.jwt;

import com.wolroys.shopgateway.validatior.RouteValidator;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {


    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RouteValidator routeValidator;

    public AuthenticationFilter(){
        super(Config.class);
    }


    @Override
    public GatewayFilter apply(AuthenticationFilter.Config config) {
        return (((exchange, chain) -> {
            if (routeValidator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION))
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access to app");

                try {
                    String token = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                    if (token != null && token.startsWith("Bearer ")) {
                        token = token.substring(7);

                        if (!jwtUtil.validateToken(token) && !jwtUtil.isExpired(token))
                            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access to app");

                        populateRequestWithHeaders(exchange, token);
                    }
                } catch (Exception e) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access to app");
                }
            }
            return chain.filter(exchange);
        }));
    }

    public static class Config{

    }

    private void populateRequestWithHeaders(ServerWebExchange exchange, String token){
        Claims claims = jwtUtil.getClaims(token);
        exchange.getRequest().mutate()
                .header("userId", claims.getSubject())
                .build();
    }
}
