package com.tech.ticketbolt.bookingservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "Booking Service API",
                version = "1.0",
                description = "API Documentation for Booking Management System"
        )
)
public class BookingServiceApplication {

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000); // 3 seconds
        factory.setReadTimeout(3000);    // 3 seconds
        return new RestTemplate(factory);
    }

    public static void main(String[] args) {
        SpringApplication.run(BookingServiceApplication.class, args);
    }

}
