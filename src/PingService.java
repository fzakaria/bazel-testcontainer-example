import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class PingService {

    public static void main(String[] args) throws IOException {
        // Create an HTTP server on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", 8080), 0);

        // Define the /ping endpoint
        server.createContext("/ping", new PingHandler());

        // Start the server
        server.setExecutor(null); // Creates a default executor
        server.start();
        System.out.println("Ping service is running on http://localhost:8080/ping");
    }

    // Handler for /ping requests
    static class PingHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "pong";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
