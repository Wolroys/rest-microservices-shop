package com.wolroys.shopgateway.validatior;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    public static final List<String> openApiEndpoints = List.of(
            "/shop/",
            "/eureka"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri)
                            || request.getURI().getPath().startsWith("/auth")
                            || request.getURI().getPath().contains("/swagger-ui")
                            || request.getURI().getPath().endsWith("/index.html")
                            || request.getURI().getPath().endsWith("/users/v3/api-docs"));
}
