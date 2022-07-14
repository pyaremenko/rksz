package com.project.network;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;

import org.reflections.Reflections;

import com.sun.net.httpserver.*;
import com.project.network.annotation.RequestMapping;
import com.project.network.annotation.RestController;
import com.project.network.dto.UserDto;
import com.project.network.executor.SQLExecutor;
import com.project.network.executor.implementation.SQLiteExecutor;
import com.project.network.filter.Filter404;
import com.project.network.filter.AuthJwtFilter;
import com.project.network.filter.EmptyHandler;
import com.project.network.filter.RequestMappingFilter;
import com.project.network.filter.SetDefaultHeadersFilter;
import com.project.network.service.UserService;
import com.project.network.service.implementation.DefaultUserService;

public class Application {

    private HttpsServer server;

    public Application() throws IOException {
        server = HttpsServer.create();
    }

    public void initializeDatabase(String name) throws SQLException {
        String createGroupTableQuery = "CREATE TABLE IF NOT EXISTS `group` (\n" +
                "`group_id`	INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "`group_name`	TEXT NOT NULL UNIQUE,\n" +
                "`group_description`	TEXT)";

        String createGoodTableQuery = "CREATE TABLE IF NOT EXISTS `good` (\n" +
                "`good_id`	INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "`good_name`	TEXT NOT NULL UNIQUE,\n" +
                "`group_id`	INTEGER NOT NULL,\n" +
                "`good_description`	TEXT,\n" +
                "`manufacturer`	TEXT,\n" +
                "`number`	INTEGER NOT NULL,\n" +
                "`price`	NUMERIC NOT NULL,\n" +
                "FOREIGN KEY(`group_id`) REFERENCES `group`(`group_id`) ON UPDATE CASCADE ON DELETE CASCADE)";
        String createUserTableQuery = "CREATE TABLE IF NOT EXISTS `user` (\n" +
                "`login`    TEXT PRIMARY KEY,\n" +
                "`password`	TEXT NOT NULL)";

        SQLiteExecutor.setDatabaseName(name);
        SQLExecutor executor = new SQLiteExecutor();
        executor.update(createGroupTableQuery);
        executor.update(createGoodTableQuery);
        executor.update(createUserTableQuery);

        UserService userService = new DefaultUserService();
        try {
            userService.createNewUser(new UserDto("root", "root"));
        } catch (Exception e) {
        }
    }

    public void createContextes() {
        Map<String, HttpContext> contextMap = new HashMap<>();
        List<String> allowWithoutAuth = Arrays.asList("/login");

        HttpContext rootContext = server.createContext("/", new EmptyHandler());
        rootContext.getFilters().add(new SetDefaultHeadersFilter());
        rootContext.getFilters().add(new AuthJwtFilter());
        rootContext.getFilters().add(new Filter404());
        contextMap.put("/", rootContext);

        Reflections reflections = new Reflections(Application.class.getPackageName());
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(RestController.class);
        for (Class<?> clazz : classes) {
            for (Method method : clazz.getMethods()) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    String path = method.getAnnotation(RequestMapping.class).path();
                    if (path.contains("{")) {
                        path = path.substring(0, path.indexOf('{'));
                    }
                    if (!contextMap.containsKey(path)) {
                        HttpContext context = server.createContext(path,
                                new EmptyHandler());
                        context.getFilters().add(new SetDefaultHeadersFilter());
                        if (!allowWithoutAuth.contains(path)) {
                            context.getFilters().add(new AuthJwtFilter());
                        }
                        contextMap.put(path, context);
                    }
                    contextMap.get(path).getFilters().add(new RequestMappingFilter(method));
                }
            }
            for (HttpContext context : contextMap.values()) {
                context.getFilters().add(new Filter404());
            }
        }
    }

    public void serverInit(){
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            char[] password = "password".toCharArray();
            KeyStore keyStore = KeyStore.getInstance("JKS");
            FileInputStream keystream = new FileInputStream("src/main/java/testkey.jks");
            keyStore.load(keystream, password);
            KeyManagerFactory keyManager = KeyManagerFactory.getInstance("SunX509");
            keyManager.init(keyStore, password);
            TrustManagerFactory trustManager = TrustManagerFactory.getInstance("SunX509");
            trustManager.init(keyStore);
            sslContext.init(keyManager.getKeyManagers(), trustManager.getTrustManagers(), null);
            server.setHttpsConfigurator(new HttpsConfigurator(sslContext) {
                public void configure(HttpsParameters params) {
                    try {
                        SSLContext sslContext = getSSLContext();
                        SSLEngine sslEngine = sslContext.createSSLEngine();
                        params.setNeedClientAuth(false);
                        params.setCipherSuites(sslEngine.getEnabledCipherSuites());
                        params.setProtocols(sslEngine.getEnabledProtocols());
                        SSLParameters sslParameters = sslContext.getSupportedSSLParameters();
                        params.setSSLParameters(sslParameters);
                    } catch (Exception e) {
                        System.out.println("Failed to create the HTTPS port");
                    }
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void start(final int port) throws IOException {
        server.bind(new InetSocketAddress(port), 0);
        System.out.println("Start listening at port " + server.getAddress().getPort());
        server.setExecutor(Executors.newFixedThreadPool(8));
        server.start();
    }

    public static void main(String[] args) throws SQLException, IOException {
        Application application = new Application();
        application.initializeDatabase("data.db");
        application.serverInit();
        application.createContextes();
        application.start(2287);
    }
}
