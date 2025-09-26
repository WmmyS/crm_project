package com.wesley.crm.config.logging;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RequestIdGenerator {

    private static final String REQUEST_ID_HEADER = "X-Request-Id";
    private static final ThreadLocal<String> REQUEST_ID_HOLDER = new ThreadLocal<>();

    public String generateRequestId() {
        return UUID.randomUUID().toString();
    }

    public void setRequestId(String requestId) {
        REQUEST_ID_HOLDER.set(requestId);
    }

    public String getCurrentRequestId() {
        return REQUEST_ID_HOLDER.get();
    }

    public void clearRequestId() {
        REQUEST_ID_HOLDER.remove();
    }

    public String getRequestIdHeader() {
        return REQUEST_ID_HEADER;
    }
}