import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import package1.db.DBProcessor;
import package1.service.ProductService;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

public class SimpleHttpServer {
    static DBProcessor dbProcessor = new DBProcessor();
    static ProductService productService = new ProductService(dbProcessor);
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    public static void renderDB() {
        dbProcessor.initialization();
    }

    public static void main(String[] args) throws Exception {

        renderDB();
        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(6968), 0);
        HttpContext context = server.createContext("/", new EchoHandler());
        context.setAuthenticator(new Auth());

        server.setExecutor(null);
        server.start();
    }


    static class EchoHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            List<Integer> existingID = productService.returnExistingID();

            if (path.startsWith("/api/good")) {
                String id = path.replace("/api/good", "").replace("/", "");
                String method = exchange.getRequestMethod();
                try {
                    if (id.isEmpty() && method.equals("GET")) {
                        process("response: 200\n" + productService.getProductDataByID(Integer.parseInt(id)).toString(), 200, exchange);
                    }
                    if (!id.isEmpty() && method.equals("GET")) {
                        if (!existingID.contains(Integer.parseInt(id))) process("Error 404", 404, exchange);
                        process("response: 200\n" + productService.getProductDataByID(Integer.parseInt(id)).toString(), 200, exchange);
                    }
                    if (id.isEmpty() && method.equals("PUT")) {
                        InputStream in = exchange.getRequestBody();
                        JSONParser jsonParser = new JSONParser();
                        JSONObject product = (JSONObject) jsonParser.parse(new InputStreamReader(in, StandardCharsets.UTF_8));
                        if (existingID.contains(Integer.parseInt(id))) process("Error 404", 404, exchange);
                        if (!checkInput(product)) {
                            process("Error 409", 409, exchange);
                        } else {
                            productService.putNewProduct(product);
                            renderDB();
                            process("response: 201 Created product with id=" + product.get("product_id"), 201, exchange);
                        }
                    }
                    if (!id.isEmpty() && method.equals("POST")) {
                        InputStream in = exchange.getRequestBody();
                        JSONParser jsonParser = new JSONParser();
                        JSONObject req = (JSONObject) jsonParser.parse(new InputStreamReader(in, StandardCharsets.UTF_8));
                        if (checkInput(req)) {
                            process("Error 409", 409,exchange);
                        } else {
                            productService.updateProduct(id, req);
                            renderDB();
                            process("response: 204 No Content", 204, exchange);
                        }
                    }
                    if (!id.isEmpty() && method.equals("DELETE")) {
                        String resp = "response: 204 No Content";
                        productService.deleteProduct(id);
                        renderDB();
                        exchange.sendResponseHeaders(204, resp.getBytes().length);
                        OutputStream os = exchange.getResponseBody();
                        os.write(resp.getBytes());
                        os.close();
                    }


                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

            }
            if (path.startsWith("/login")) {
                InputStream in = exchange.getRequestBody();
                User user = OBJECT_MAPPER.readValue(in, User.class);
                if(Objects.equals(user.getLogin(), "root") && Objects.equals(user.getPassword(), "root")){
                    String jwt = JWT.createJWT(user.login);
                    exchange.getResponseHeaders().add("jwt", jwt);
//                    exchange.getRequestHeaders().add("jwt", jwt);
                    System.out.println(exchange.getRequestBody().toString());

                    // input jwt to headers in postman (jwt : jwt)
                    process("{\"jwt\" : "+ jwt +" }", 200, exchange);
                } else {
                    process("Error 404", 404, exchange);
                }

            }
        }

        public void process(String str, int rcode, HttpExchange exchange) throws IOException {
            exchange.sendResponseHeaders(rcode, str.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(str.getBytes());
            os.close();
        }

        public boolean checkInput(JSONObject product) {
            if (product.containsKey("product_id") && ((long) product.get("product_id") >= 0)) {
                return false;
            }
            if (product.containsKey("product_name") && (product.get("product_id").toString().isEmpty())) {
                return false;
            }
            if (product.containsKey("type") && ((long) product.get("type") >= 0)) {
                return false;
            }
            if (product.containsKey("amount") && ((long) product.get("amount") >= 0)) {
                return false;
            }
            if (product.containsKey("price") && ((long) product.get("price") >= 0)) {
                return false;
            }
            return true;
        }
    }

    static class Auth extends Authenticator {
        @Override
        public Result authenticate(HttpExchange httpExchange) {
            String path = httpExchange.getRequestURI().getPath();
            if (path.equals("/login")) return new Success(new HttpPrincipal("c0nst", "realm"));
            String jwt = String.valueOf(httpExchange.getRequestHeaders().getFirst("jwt"));
            if(jwt.equals(null)){
                return new Failure(403);
            }
            String login = JWT.extractUsername(jwt);
            if(login != "root")return new Failure(403);
            else
                return new Success(new HttpPrincipal("c0nst", "realm"));
        }
    }
    public static class User{
        String login;
        String password;

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

}
