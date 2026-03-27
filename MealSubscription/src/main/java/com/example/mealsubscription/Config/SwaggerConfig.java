package com.example.mealsubscription.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customConfig(){
        return new OpenAPI()
                .info(
                        new Info().title("Meal Subscription App")
                                .description("User subscribe to different meal slots")
                );

    }
}
