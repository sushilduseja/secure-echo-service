
package com.example.secureecho.model;

public class AuthRequest {
    private String clientId;
    private String apiKey;

    public AuthRequest() {
    }

    public AuthRequest(String clientId, String apiKey) {
        this.clientId = clientId;
        this.apiKey = apiKey;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
