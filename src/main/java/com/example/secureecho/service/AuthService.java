package com.example.secureecho.service;

import com.example.secureecho.config.ClientProperties;
import com.example.secureecho.model.AuthRequest;
import com.example.secureecho.model.AuthResponse;
import com.example.secureecho.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final JwtUtil jwtUtil;
    private final ClientProperties clientProperties;
    
    @Value("${jwt.expiration}")
    private long expirationTime;
    
    // Load client configurations from external configuration
    private Map<String, ClientProperties.ClientConfig> clientConfigs;
    
    public AuthService(JwtUtil jwtUtil, ClientProperties clientProperties) {
        this.jwtUtil = jwtUtil;
        this.clientProperties = clientProperties;
        
        // Initialize client configs
        initializeClientConfigs();
    }
    
    @PostConstruct
    public void logClientConfigs() {
        // Log available client IDs for debugging
        logger.info("Loaded {} client configurations", clientConfigs.size());
        clientConfigs.keySet().forEach(id -> {
            ClientProperties.ClientConfig config = clientConfigs.get(id);
            logger.info("Client configured: {} with API key: {} (length: {}) and roles: {}", 
                id, 
                config.getApiKey().substring(0, Math.min(5, config.getApiKey().length())) + "...", 
                config.getApiKey().length(),
                config.getRoles());
        });
    }
    
    private void initializeClientConfigs() {
        this.clientConfigs = clientProperties.getList().stream()
                .collect(Collectors.toMap(
                        ClientProperties.ClientConfig::getId,
                        Function.identity()
                ));
    }
    
    public AuthResponse authenticate(AuthRequest request) {
        logger.debug("Authentication request received for client: {}", request.getClientId());
        
        // Map client IDs from the UI format to the configuration format
        String clientId = mapClientId(request.getClientId());
        
        ClientProperties.ClientConfig config = clientConfigs.get(clientId);
        
        if (config == null) {
            logger.warn("Authentication failed: Client ID not found: {}", clientId);
            logger.debug("Available client IDs: {}", clientConfigs.keySet());
            throw new BadCredentialsException("Invalid client credentials");
        }
        
        logger.debug("Comparing API keys - Request key: {} (length: {}), Config key: {} (length: {})",
            request.getApiKey().substring(0, Math.min(5, request.getApiKey().length())) + "...",
            request.getApiKey().length(),
            config.getApiKey().substring(0, Math.min(5, config.getApiKey().length())) + "...",
            config.getApiKey().length());
            
        if (!config.getApiKey().equals(request.getApiKey())) {
            logger.warn("Authentication failed: Invalid API key for client: {}", clientId);
            throw new BadCredentialsException("Invalid client credentials");
        }
        
        logger.info("Authentication successful for client: {}", clientId);
        String token = jwtUtil.generateToken(clientId, config.getRoles());
        return new AuthResponse(token, expirationTime / 1000);
    }
    
    // Map client IDs from the request format to the configuration format
    private String mapClientId(String requestClientId) {
        if ("user-client".equalsIgnoreCase(requestClientId)) {
            return "user-client";
        } else if ("admin-client".equalsIgnoreCase(requestClientId)) {
            return "admin-client";
        }
        return requestClientId;
    }
}
