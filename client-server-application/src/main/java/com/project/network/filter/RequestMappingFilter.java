package com.project.network.filter;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.project.network.annotation.PathParameter;
import com.project.network.annotation.RequestBody;
import com.project.network.annotation.RequestMapping;
import com.project.network.annotation.RequestParameter;
import com.project.network.exception.ResponseErrorException;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

public class RequestMappingFilter extends Filter {
    private static final Gson gson = new Gson();

    private Method handler;

    public RequestMappingFilter(Method handler) {
        super();
        this.handler = handler;
    }

    private Map<String, String> parseRequestParameters(String requestQuery) throws UnsupportedEncodingException {
        Map<String, String> parameterMap = new HashMap<>();
        if (requestQuery != null) {
            String[] pairs = requestQuery.split("\\&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                parameterMap.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
                        URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
            }
        }
        return parameterMap;
    }

    private Map<String, String> comparePaths(String handlerPath, String requestPath) {
        String[] handlerPathArray = handlerPath.split("\\/");
        String[] requestPathArray = requestPath.split("\\/");
        if (handlerPathArray.length != requestPathArray.length || requestPath.endsWith("/")) {
            return null;
        }
        Map<String, String> parameterMap = new HashMap<>();
        for (int i = 0; i < handlerPathArray.length; ++i) {
            String path = handlerPathArray[i];
            if (path.startsWith("{") && path.endsWith("}")) {
                parameterMap.put(path.substring(1, path.length() - 1), requestPathArray[i]);
            } else if (!path.equals(requestPathArray[i])) {
                return null;
            }
        }
        return parameterMap;
    }

    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        try {
            Map<String, String> pathParameters = comparePaths(
                    handler.getAnnotation(RequestMapping.class).path(),
                    exchange.getRequestURI().getPath().trim());
            if (pathParameters == null) {
                chain.doFilter(exchange);
                return;
            } else if (!handler.getAnnotation(RequestMapping.class).method().name()
                    .equals(exchange.getRequestMethod())) {
                chain.doFilter(exchange);
                return;
            } else {

                Map<String, String> requestParameters = parseRequestParameters(exchange.getRequestURI().getQuery());

                List<Object> handlerParameters = new LinkedList<>();
                Parameter[] parameters = handler.getParameters();
                for (Parameter param : parameters) {
                    PathParameter pathParameter = param.getAnnotation(PathParameter.class);
                    if (pathParameter != null) {
                        handlerParameters.add(pathParameters.get(pathParameter.value()));
                    }
                    RequestBody bodyParameter = param.getAnnotation(RequestBody.class);
                    if (bodyParameter != null) {
                        InputStream input = exchange.getRequestBody();
                        String body = new String(input.readAllBytes());
                        if (body.isEmpty()) {
                            throw new ResponseErrorException(400, "Request body is required");
                        }
                        try {
                            handlerParameters.add(gson.fromJson(body, param.getType()));
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                            throw new ResponseErrorException(400, "Bad body json syntax");
                        }
                    }
                    RequestParameter requestParameter = param.getAnnotation(RequestParameter.class);
                    if (requestParameter != null) {
                        String key = requestParameter.value();
                        if (requestParameter.require() && !requestParameters.containsKey(key)) {
                            throw new ResponseErrorException(400, key + " query parameter is required");
                        } else {
                            handlerParameters.add(requestParameters.get(key));
                        }
                    }
                }

                Constructor<?> constructor;
                constructor = handler.getDeclaringClass().getConstructor();
                Object response = handler.invoke(constructor.newInstance(), handlerParameters.toArray());
                ResponseSender.sendResponse(exchange, 200, response);
            }
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof ResponseErrorException) {
                ResponseErrorException errorException = (ResponseErrorException) e.getCause();
                ResponseSender.sendErrorMessage(exchange, errorException.getStatusCode(),
                        errorException.getMessage());
            } else {
                e.printStackTrace();
                ResponseSender.sendErrorMessage(exchange, 500, "Internal server error");
            }
        } catch (ResponseErrorException e) {
            ResponseSender.sendErrorMessage(exchange, e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            ResponseSender.sendErrorMessage(exchange, 500, "Internal server error");
            return;
        }
    }

    @Override
    public String description() {
        return "Filter checks if request path match method mapping and send response if match";
    }

}
