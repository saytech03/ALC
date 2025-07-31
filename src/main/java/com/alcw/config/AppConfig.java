package com.alcw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration // Marks this class as a source of bean definitions
public class AppConfig {

    @Bean // Tells Spring to create a bean from the return value of this method
    public RestTemplate restTemplate() {
        // You can customize the RestTemplate here if needed (e.g., timeouts, message converters)
        return new RestTemplate();
    }
}
