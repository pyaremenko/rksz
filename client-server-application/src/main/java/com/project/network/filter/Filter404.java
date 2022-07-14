package com.project.network.filter;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.Filter;

public class Filter404 extends Filter {
    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        try {
            ResponseSender.sendErrorMessage(exchange, 404, "Path not found");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String description() {
        return "Send 404 error to client";
    }
}
