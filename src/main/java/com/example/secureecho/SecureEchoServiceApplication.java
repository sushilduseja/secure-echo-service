package com.example.secureecho;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.secureecho"})
public class SecureEchoServiceApplication {

    public static void main(String[] args) {
        System.setProperty("spring.config.import", "optional:file:.env[.properties]");
        SpringApplication.run(SecureEchoServiceApplication.class, args);
    }
}