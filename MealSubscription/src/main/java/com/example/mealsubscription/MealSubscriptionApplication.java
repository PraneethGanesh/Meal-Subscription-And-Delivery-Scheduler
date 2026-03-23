package com.example.mealsubscription;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MealSubscriptionApplication {

    public static void main(String[] args) {
        SpringApplication.run(MealSubscriptionApplication.class, args);
    }

}
