package com.example.secureecho.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    @GetMapping("/hello")
    public Map<String, String> hello() {
        return Map.of(
            "message", "Hello from Secure Echo Service!",
            "status", "Public endpoint accessible to all"
        );
    }
    
    @GetMapping("/info")
    public Map<String, Object> info() {
        return Map.of(
            "service", "Secure Echo Service",
            "version", "1.0.0",
            "description", "A secure echo service with JWT authentication and role-based authorization"
        );
    }
}
