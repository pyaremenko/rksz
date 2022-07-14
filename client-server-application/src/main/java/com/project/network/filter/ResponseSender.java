package com.project.network.filter;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;

import com.google.gson.Gson;
import com.project.network.dto.ErrorMessageDto;
import com.sun.net.httpserver.HttpExchange;

public class ResponseSender {
    private static final Gson gson = new Gson();

    /* private static void writeHeaders(HttpExchange exchange, Map<String, String> headers) {
        if (headers != null) {
            for (String key : headers.keySet()) {
                exchange.getResponseHeaders().add(key, headers.get(key));
            }
        }
    } */

    public static void sendErrorMessage(HttpExchange exchange, int statusCode, String message) throws IOException {
        ErrorMessageDto errorMessageDto = new ErrorMessageDto(statusCode, message,
                new Timestamp(System.currentTimeMillis()));
        String response = gson.toJson(errorMessageDto);
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public static void sendResponse(HttpExchange exchange, int status, Object responseObject) throws IOException {
        String response = null;
        if (responseObject != null) {
            response = gson.toJson(responseObject);
        } else {
            response = "";
        }
        exchange.sendResponseHeaders(status, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
