    package com.loanmanagement.gateway.config;

    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.cloud.gateway.route.RouteLocator;
    import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.http.HttpHeaders;
    import org.springframework.web.bind.annotation.CrossOrigin;
    import org.springframework.web.bind.annotation.RequestMethod;
    import org.springframework.web.bind.annotation.RestController;
    import reactor.core.publisher.Mono;

    import javax.servlet.annotation.WebFilter;


    @Configuration
    @CrossOrigin(origins = "http://localhost:4200")  // Update the origin as per your setup
    @RestController

    public class ApiGatewayConfig {

        private static final Logger logger = LoggerFactory.getLogger(ApiGatewayConfig.class);


        @Bean
        public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
            logger.info("Initializing API Gateway Routes...");

            return builder.routes()
                    // Route for checking customer loans (POST method)
                    .route("loan-service-check-customer", r -> r.path("/api/loans/check-customer")
                            .and().method("POST")
                            .filters(f -> f.addRequestHeader("X-Custom-Header", "Loan-Check")
                                    .modifyRequestBody(String.class, String.class, (exchange, requestBody) -> {
                                        System.out.println("Request Body: " + requestBody); // Log request body
                                        return Mono.just(requestBody);  // Forward the request body
                                    }))
                            .uri("http://localhost:8081/api/loans/check-customer"))


                    // Route for calculating max loan (POST method)
                    .route("loan-service-calculate-max-loan", r -> r.path("/api/loans/calculate-max-loan")
                    .and().method("POST")
                    .filters(f -> f.addRequestHeader("Content-Type", "application/json")
                            .modifyResponseBody(String.class, String.class, (exchange, responseBody) -> {
                                // Log the response body to the console
                                System.out.println("Response from calculate-max-loan API: " + responseBody);
                                return Mono.just(responseBody);  // Return the response as-is
                            }))
                    .uri("http://localhost:8081/api/loans/calculate-max-loan")) // Forward request to the backend API

                    // Add a filter to rewrite the path by stripping `/gateway-api` from the request path
                       .build();
        }
    }

