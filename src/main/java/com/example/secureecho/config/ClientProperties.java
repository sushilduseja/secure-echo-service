package com.example.secureecho.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "security.clients")
public class ClientProperties {
    private List<ClientConfig> list = new ArrayList<>();

    public List<ClientConfig> getList() {
        return list;
    }

    public void setList(List<ClientConfig> list) {
        this.list = list;
    }

    public static class ClientConfig {
        private String id;
        private String apiKey;
        private List<String> roles = new ArrayList<>();

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public List<String> getRoles() {
            return roles;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }
    }
}
