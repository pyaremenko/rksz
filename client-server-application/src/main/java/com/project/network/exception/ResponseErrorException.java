package com.project.network.exception;

import java.util.HashMap;
import java.util.Map;

public class ResponseErrorException extends RuntimeException {
    private static final Map<Integer, String> defaultMessages;

    static {
        defaultMessages = new HashMap<>();
        defaultMessages.put(404, "Not found");
        defaultMessages.put(500, "Internal server error");
    }

    private static String getMessageByCode(final int code) {
        return defaultMessages.get(code) == null ? "Unknown error" : defaultMessages.get(code);
    }

    private int statusCode;

    public ResponseErrorException(final int statusCode, final String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public ResponseErrorException(final int statusCode) {
        super(getMessageByCode(statusCode));
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }    
}
