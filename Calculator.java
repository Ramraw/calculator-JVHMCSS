import java.io.IOException;
import java.io.OutputStream;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class Calculator {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new CalculatorHandler());
        server.start();
        System.out.println("Server is listening on port 8000");
    }

    static class CalculatorHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();

            if (method.equalsIgnoreCase("POST")) {
                String requestBody = new String(exchange.getRequestBody().readAllBytes());
                String[] parts = requestBody.split("&");
                double num1 = Double.parseDouble(parts[0].split("=")[1]);
                double num2 = Double.parseDouble(parts[1].split("=")[1]);
                String operator = parts[2].split("=")[1];

                double result = calculate(num1, num2, operator);

                String response = String.valueOf(result);
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }

        private double calculate(double num1, double num2, String operator) {
            switch (operator) {
                case "add":
                    return num1 + num2;
                case "subtract":
                    return num1 - num2;
                case "multiply":
                    return num1 * num2;
                case "divide":
                    return num1 / num2;
                default:
                    return 0;
            }
        }
    }
}
