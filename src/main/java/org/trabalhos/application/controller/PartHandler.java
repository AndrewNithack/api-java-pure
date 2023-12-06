package org.trabalhos.application.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.trabalhos.application.model.Part;
import org.trabalhos.infraestructure.repository.PartRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Logger;

import static org.trabalhos.util.JsonConverterUtil.*;

public class PartHandler implements HttpHandler {

    PartRepository partRepository;

    public PartHandler(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET" -> {
                logger.info("GET");
                int validation = exchange.getRequestURI().getPath().split("/").length;
                if (validation == 2) {
                    listPart(exchange);
                }
                if (validation == 3)
                    getPart(exchange);
            }
            case "POST" -> {
                logger.info("POST");
                createPart(exchange);
            }
            case "PUT" -> {
                logger.info("PUT");
                updatePart(exchange);
            }
            case "DELETE" -> {
                logger.info("DELETE");
                deletePart(exchange);
            }
            default -> {
                logger.info("Método não suportado: " + method);
                sendResponse(exchange, 405, "Método não suportado: " + method);
            }
        }
    }

    private void getPart(HttpExchange exchange) throws IOException {
        try {
            String id = exchange.getRequestURI().getPath().split("/")[2];
            Part part = partRepository.getById(id);
            if (part == null) {
                logger.info("Peça não encontrado");
                sendResponse(exchange, 404, "Peça não encontrado");
                return;
            }
            String responseText = convertToJson(part);
            sendResponse(exchange, 200, responseText);
        } catch (Exception e) {
            logger.info("Erro ao buscar Peça: " + e.getMessage());
            sendResponse(exchange, 400, "Erro ao buscar Peça: " + e.getMessage());
        }
    }

    private void listPart(HttpExchange exchange) throws IOException {
        try {
            List<Part> parts = partRepository.list();
            if (parts == null) {
                logger.info("Nenhuma Peça encontrada");
                sendResponse(exchange, 404, "Nenhuma Peça encontrada");
                return;
            }
            String responseText = convertArrayToJson(parts);
            sendResponse(exchange, 200, responseText);
        } catch (Exception e) {
            logger.info("Erro ao buscar Peça: " + e.getMessage());
            sendResponse(exchange, 400, "Erro ao buscar Peça: " + e.getMessage());
        }
    }

    private void createPart(HttpExchange exchange) throws IOException {
        try {
            String body = new String(exchange.getRequestBody().readAllBytes());
            Part part = convertFromJson(body, Part.class);

            boolean result = partRepository.insert(part);
            if (!result) {
                logger.info("Erro ao inserir Peça");
                sendResponse(exchange, 400, "Erro ao inserir Peça");
                return;
            }
            logger.info("Peça inserido com sucesso");
            sendResponse(exchange, 201, "Peça inserido com sucesso");
        } catch (Exception e) {
            logger.info("Erro ao criar Peça: " + e.getMessage());
            sendResponse(exchange, 400, "Erro ao criar Peça: " + e.getMessage());
        }
    }

    private void deletePart(HttpExchange exchange) throws IOException {
        try {
            String id = exchange.getRequestURI().getPath().split("/")[2];
            Part part = partRepository.getById(id);
            if (part == null) {
                logger.info("Peça não encontrado");
                sendResponse(exchange, 404, "Peça não encontrado");
                return;
            }
            boolean result = partRepository.delete(id);
            if (!result) {
                logger.info("Peça não encontrado id: " + id);
                sendResponse(exchange, 400, "Erro ao deletar Peça");
                return;
            }
            logger.info("Peça deletado com sucesso");
            sendResponse(exchange, 201, "Peça deletado com sucesso");
        } catch (Exception e) {
            logger.info("Erro ao deletar Peça: " + e.getMessage());
            sendResponse(exchange, 400, "Erro ao deletar Peça: " + e.getMessage());
        }
    }

    private void updatePart(HttpExchange exchange) throws IOException {
        try {
            String id = exchange.getRequestURI().getPath().split("/")[2];
            Part part = partRepository.getById(id);
            if (part == null) {
                logger.info("Peça não encontrado");
                sendResponse(exchange, 404, "Peça não encontrado");
                return;
            }
            String body = new String(exchange.getRequestBody().readAllBytes());
            Part partUpdate = convertFromJson(body, Part.class);
            boolean result = partRepository.update(partUpdate, id);
            if (!result) {
                logger.info("Peça não encontrado id: " + id);
                sendResponse(exchange, 400, "Erro ao deletar Peça");
                return;
            }
            logger.info("Peça atualizado com sucesso");
            sendResponse(exchange, 201, "Peça atualizado com sucesso");
        } catch (Exception e) {
            logger.info("Erro ao atualizar Peça: " + e.getMessage());
            sendResponse(exchange, 400, "Erro ao atualizar Peça: " + e.getMessage());
        }
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String responseText) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, responseText.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(responseText.getBytes());
        logger.info("Enviando resposta: " + responseText);
        os.close();
    }
}
