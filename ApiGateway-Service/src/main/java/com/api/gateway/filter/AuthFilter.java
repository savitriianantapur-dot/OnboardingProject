package com.api.gateway.filter;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.api.gateway.service.JwtService;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    @Autowired
    private RouteValidator routeValidator;

   @Autowired
    private JwtService jwtService;

    public AuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            ServerWebExchange requestExchange = exchange;

            // 1️⃣ Skip JWT check for public endpoints
            if (!routeValidator.isSecured(exchange.getRequest())) {
                // Public route, skip JWT check
                return chain.filter(exchange);
            }


            // 2️⃣ Get Authorization header
            List<String> authHeaders = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
            if (authHeaders == null || authHeaders.isEmpty()) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            // 3️⃣ Extract Bearer token
            String authHeader = authHeaders.get(0);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String token = authHeader.substring(7).trim();

            // 4️⃣ Validate token using JwtService
            try {
                jwtService.isTokenValid(token); // should throw if invalid
            } catch (Exception ex) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            // 5️⃣ Token valid → continue to downstream service
            return chain.filter(exchange);
        };
    }

    public static class Config {
        // Place config properties here if needed
    }
}

