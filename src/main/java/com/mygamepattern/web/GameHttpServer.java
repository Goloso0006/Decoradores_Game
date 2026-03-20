package com.mygamepattern.web;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Servidor HTTP simple embebido con endpoints REST para el juego RPG.
 */
public class GameHttpServer {
    private HttpServer server;
    private Gson gson;
    private int port;

    public GameHttpServer(int port) {
        this.port = port;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new HomeHandler());
        server.createContext("/api/status", new StatusHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Servidor HTTP iniciado en http://localhost:" + port);
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    private class HomeHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String html = "<!DOCTYPE html><html><head><meta charset='UTF-8'>" +
                    "<title>MyGamePattern - RPG</title>" +
                    "<style>body{font-family:Arial;margin:20px;}</style>" +
                    "</head><body>" +
                    "<h1>MyGamePattern - RPG Combat</h1>" +
                    "<p>Servidor HTTP embebido funcionando.</p>" +
                    "<a href='/api/status'>Ver status</a>" +
                    "</body></html>";

            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, html.length());
            OutputStream os = exchange.getResponseBody();
            os.write(html.getBytes());
            os.close();
        }
    }

    private class StatusHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "ok");
            response.put("server", "MyGamePattern RPG");
            response.put("version", "1.0");

            String jsonResponse = gson.toJson(response);
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(200, jsonResponse.length());
            OutputStream os = exchange.getResponseBody();
            os.write(jsonResponse.getBytes());
            os.close();
        }
    }
}

