package org.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiWorkApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiWorkApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(ApiService apiService) {

        return new CommandLineRunner() {

            @Override
            public void run(String... args) throws Exception {
                apiService.executeFlow();
            }
        };
    }
}