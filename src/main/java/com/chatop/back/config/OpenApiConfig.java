package com.chatop.back.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("ChatOp API")
                .version("1.0")
                .description("Documentation de l'API ChatOp")
                .license(new License().name("Apache 2.0").url("http://springdoc.org"))
            );
    }
}
