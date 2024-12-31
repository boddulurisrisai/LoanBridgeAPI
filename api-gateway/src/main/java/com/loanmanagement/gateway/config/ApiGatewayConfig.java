package com.loanmanagement.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class ApiGatewayConfig {

    private static final Logger logger = LoggerFactory.getLogger(ApiGatewayConfig.class);

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        logger.info("Initializing API Gateway Routes...");

        return builder.routes()
                // Route for checking customer loans (POST method)
                .route("loan-service-check-customer", r -> r.path("/gateway-api/loans/check-customer")
                        .and().method("POST")
                        .filters(f -> f.addRequestHeader("X-Custom-Header", "Loan-Check"))
                        .uri("http://localhost:8081/api/loans/check-customer")) // Forward request to the backend API

                // Route for calculating max loan (POST method)
                .route("loan-service-calculate-max-loan", r -> r.path("/gateway-api/loans/calculate-max-loan")
                        .and().method("POST")
                        .filters(f -> f.addRequestHeader("X-Custom-Header", "Max-Loan")
                                .addRequestHeader("Content-Type", "application/json"))
                        .uri("http://localhost:8081/api/loans/calculate-max-loan")) // Forward request to the backend API

                // Add a filter to rewrite the path by stripping `/gateway-api` from the request path
                .route("loan-service-calculate-max-loan-rewrite", r -> r.path("/gateway-api/**")
                        .filters(f -> f.rewritePath("/gateway-api/(?<segment>.*)", "/${segment}"))
                        .uri("http://localhost:8081"))
                .build();
    }
}