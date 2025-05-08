package com.example.secureecho.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class DotenvConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(DotenvConfig.class);
    
    public DotenvConfig() {
        loadDotEnv(); // Load during construction to ensure variables are available early
    }
    
    @PostConstruct
    public void init() {
        logger.debug("DotenvConfig initialized");
    }
    
    private void loadDotEnv() {
        try {
            // First, check if .env exists in the current directory
            Path dotenvPath = Paths.get(".env");
            
            if (Files.exists(dotenvPath)) {
                // If .env exists, load it
                Dotenv dotenv = Dotenv.configure().load();
                logger.debug("Contents of .env file:");
                dotenv.entries().forEach(entry -> {
                    System.setProperty(entry.getKey(), entry.getValue());
                    logger.debug("Set property {} from .env file with value length: {}", 
                        entry.getKey(), entry.getValue().length());
                });
                logger.info("Loaded environment variables from .env file");
            } else {
                // Otherwise, log a warning
                logger.warn(".env file not found. Using default or environment values.");
                logger.info("For local development, create a .env file in the project root directory.");
            }
        } catch (Exception e) {
            logger.error("Error loading .env file: {}", e.getMessage(), e);
        }
    }
}
