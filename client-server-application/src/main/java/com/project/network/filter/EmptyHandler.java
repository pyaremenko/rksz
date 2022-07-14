package com.project.network.filter;

import java.io.IOException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import lombok.Setter;

@Setter
public class EmptyHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        ResponseSender.sendErrorMessage(exchange, 500, "Internal server error");
    }
}
