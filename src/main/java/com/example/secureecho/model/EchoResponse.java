package com.example.secureecho.model;

import java.time.LocalDateTime;

public class EchoResponse {
    private String message;
    private LocalDateTime timestamp;
    private String echoedBy;

    public EchoResponse(String message, String echoedBy) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.echoedBy = echoedBy;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getEchoedBy() {
        return echoedBy;
    }

    public void setEchoedBy(String echoedBy) {
        this.echoedBy = echoedBy;
    }
}
