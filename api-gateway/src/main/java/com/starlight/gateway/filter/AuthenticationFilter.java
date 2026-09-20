package com.starlight.gateway.filter;

import com.starlight.gateway.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RouteValidator validator;
    private final JwtUtil jwtUtil;

    public AuthenticationFilter(RouteValidator validator, JwtUtil jwtUtil) {
        super(Config.class);
        this.validator = validator;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (validator.isSecured.test(request)) {
                if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    return onError(exchange, "Missing Authorization Header", HttpStatus.UNAUTHORIZED);
                }

                String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    return onError(exchange, "Invalid Authorization Header format", HttpStatus.UNAUTHORIZED);
                }

                String token = authHeader.substring(7);
                if (!jwtUtil.validateToken(token)) {
                    return onError(exchange, "Unauthorized access: invalid or expired token", HttpStatus.UNAUTHORIZED);
                }

                try {
                    Claims claims = jwtUtil.getClaims(token);
                    String username = claims.getSubject();
                    Object userId = claims.get("userId");
                    Object email = claims.get("email");
                    Object role = claims.get("role");

                    // Mutate request with user context headers for downstream microservices
                    ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                            .header("X-User-Username", username != null ? username : "")
                            .header("X-User-Id", userId != null ? String.valueOf(userId) : "")
                            .header("X-User-Email", email != null ? String.valueOf(email) : "")
                            .header("X-User-Roles", role != null ? String.valueOf(role) : "")
                            .build();

                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                } catch (Exception e) {
                    return onError(exchange, "Token processing error", HttpStatus.UNAUTHORIZED);
                }
            }

            return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    public static class Config {
        // Configuration properties if needed
    }
}
