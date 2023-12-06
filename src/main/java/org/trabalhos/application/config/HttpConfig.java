package org.trabalhos.application.config;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

import static java.lang.String.format;

public class HttpConfig {

    Logger logger = Logger.getLogger(getClass().getName());
    private HttpServer server;
    public HttpConfig(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
    }

    public void start()  {
        server.setExecutor(null);
        server.start();
        logger.info("Servidor HTTP iniciado na porta " + server.getAddress().getPort());
    }

    public void registerRoutes(String name, HttpHandler handler){
        server.createContext(name, handler);
        logger.info("Registrando rota "+ name);
    }
}
