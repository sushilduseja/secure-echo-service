package com.example.secureecho.service;

import com.example.secureecho.model.AuthRequest;
import com.example.secureecho.model.AuthResponse;
import com.example.secureecho.security.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    private final JwtUtil jwtUtil;
    
    @Value("${jwt.expiration}")
    private long expirationTime;
    
    // In a real application, these would be stored securely, not hard-coded
    private final Map<String, ClientConfig> clientConfigs;
    
    static class ClientConfig {
        final String apiKey;
        final List<String> roles;
        
        ClientConfig(String apiKey, List<String> roles) {
            this.apiKey = apiKey;
            this.roles = roles;
        }
    }
    
    public AuthService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        
        // Initialize some test clients
        clientConfigs = new HashMap<>();
        clientConfigs.put("user-client", new ClientConfig("user-secret-key", List.of("USER_ROLE")));
        clientConfigs.put("admin-client", new ClientConfig("admin-secret-key", List.of("USER_ROLE", "ADMIN_ROLE")));
    }
    
    public AuthResponse authenticate(AuthRequest request) {
        ClientConfig config = clientConfigs.get(request.getClientId());
        
        if (config == null || !config.apiKey.equals(request.getApiKey())) {
            throw new BadCredentialsException("Invalid client credentials");
        }
        
        String token = jwtUtil.generateToken(request.getClientId(), config.roles);
        return new AuthResponse(token, expirationTime / 1000);
    }
}
