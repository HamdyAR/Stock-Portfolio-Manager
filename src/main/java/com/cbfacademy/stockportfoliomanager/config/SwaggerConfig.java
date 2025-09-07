package com.cbfacademy.stockportfoliomanager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI stockPortfolioOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Stock Portfolio Manager API")
                        .description("A REST API for managing stock portfolios with order tracking and portfolio calculation")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Stock Portfolio Manager")
                                .url("https://github.com/HamdyAR/Stock-Portfolio-Manager")));
    }
}
