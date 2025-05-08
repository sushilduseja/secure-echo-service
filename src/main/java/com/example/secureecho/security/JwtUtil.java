package com.example.secureecho.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expirationTime;
    
    private Algorithm algorithm;
    
    @PostConstruct
    public void init() {
        // Check if secret is loaded properly
        if (secret == null || secret.equals("default-dev-only-secret-key-replace-in-production")) {
            logger.warn("JWT secret not properly configured! Using default value, which is not secure.");
        } else {
            logger.info("JWT secret configured successfully with length: {}", secret.length());
        }
        algorithm = Algorithm.HMAC256(secret);
    }

    public String generateToken(String clientId, List<String> roles) {
        logger.debug("Generating token for client: {} with roles: {}", clientId, roles);
        
        return JWT.create()
                .withSubject(clientId)
                .withClaim("roles", roles)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(algorithm);
    }

    public Authentication validateTokenAndGetAuthentication(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            
            String username = decodedJWT.getSubject();
            List<String> roles = decodedJWT.getClaim("roles").asList(String.class);
            
            List<GrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            
            logger.debug("Successfully validated token for user: {} with roles: {}", 
                username, authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(", ")));
                
            return new UsernamePasswordAuthenticationToken(username, null, authorities);
        } catch (JWTVerificationException exception) {
            logger.error("Token validation failed: {}", exception.getMessage());
            return null;
        }
    }
}
