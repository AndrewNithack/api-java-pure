package org.trabalhos.application.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.trabalhos.application.model.Manufacture;
import org.trabalhos.infraestructure.repository.ManufactureRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Logger;

import static org.trabalhos.util.JsonConverterUtil.*;

public class ManufactureHandler implements HttpHandler {

    ManufactureRepository manufactureRepository;

    public ManufactureHandler(ManufactureRepository manufactureRepository) {
        this.manufactureRepository = manufactureRepository;
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
                    listManufacture(exchange);
                }
                if (validation == 3)
                    getManufacture(exchange);
            }
            case "POST" -> {
                logger.info("POST");
                createManufacture(exchange);
            }
            case "PUT" -> {
                logger.info("PUT");
                updateManufacture(exchange);
            }
            case "DELETE" -> {
                logger.info("DELETE");
                deleteManufacture(exchange);
            }
            default -> {
                logger.info("Método não suportado: " + method);
                sendResponse(exchange, 405, "Método não suportado: " + method);
            }
        }
    }

    private void getManufacture(HttpExchange exchange) throws IOException {
        try {
            String id = exchange.getRequestURI().getPath().split("/")[2];
            Manufacture manufacture = manufactureRepository.getById(id);
            if (manufacture == null) {
                logger.info("Fabricante não encontrado");
                sendResponse(exchange, 404, "Fabricante não encontrado");
                return;
            }
            String responseText = convertToJson(manufacture);
            sendResponse(exchange, 200, responseText);
        } catch (Exception e) {
            logger.info("Erro ao buscar Fabricante: " + e.getMessage());
            sendResponse(exchange, 400, "Erro ao buscar Fabricante: " + e.getMessage());
        }
    }

    private void listManufacture(HttpExchange exchange) throws IOException {
        try {
            List<Manufacture> manufactures = manufactureRepository.list();
            if (manufactures == null) {
                logger.info("Nenhuma Fabricante encontrada");
                sendResponse(exchange, 404, "Nenhuma Fabricante encontrada");
                return;
            }
            String responseText = convertArrayToJson(manufactures);
            sendResponse(exchange, 200, responseText);
        } catch (Exception e) {
            logger.info("Erro ao buscar Fabricante: " + e.getMessage());
            sendResponse(exchange, 400, "Erro ao buscar Fabricante: " + e.getMessage());
        }
    }

    private void createManufacture(HttpExchange exchange) throws IOException {
        try {
            String body = new String(exchange.getRequestBody().readAllBytes());
            Manufacture manufacture = convertFromJson(body, Manufacture.class);

            boolean result = manufactureRepository.insert(manufacture);
            if (!result) {
                logger.info("Erro ao inserir Fabricante");
                sendResponse(exchange, 400, "Erro ao inserir Fabricante");
                return;
            }
            logger.info("Fabricante inserido com sucesso");
            sendResponse(exchange, 201, "Fabricante inserido com sucesso");
        } catch (Exception e) {
            logger.info("Erro ao criar Fabricante: " + e.getMessage());
            sendResponse(exchange, 400, "Erro ao criar Fabricante: " + e.getMessage());
        }
    }

    private void deleteManufacture(HttpExchange exchange) throws IOException {
        try {
            String id = exchange.getRequestURI().getPath().split("/")[2];
            Manufacture manufacture = manufactureRepository.getById(id);
            if (manufacture == null) {
                logger.info("Fabricante não encontrado");
                sendResponse(exchange, 404, "Fabricante não encontrado");
                return;
            }
            boolean result = manufactureRepository.delete(id);
            if (!result) {
                logger.info("Fabricante não encontrado id: " + id);
                sendResponse(exchange, 400, "Erro ao deletar Fabricante");
                return;
            }
            logger.info("Fabricante deletado com sucesso");
            sendResponse(exchange, 201, "Fabricante deletado com sucesso");
        } catch (Exception e) {
            logger.info("Erro ao deletar Fabricante: " + e.getMessage());
            sendResponse(exchange, 400, "Erro ao deletar Fabricante: " + e.getMessage());
        }
    }

    private void updateManufacture(HttpExchange exchange) throws IOException {
        try {
            String id = exchange.getRequestURI().getPath().split("/")[2];
            Manufacture manufacture = manufactureRepository.getById(id);
            if (manufacture == null) {
                logger.info("Fabricante não encontrado");
                sendResponse(exchange, 404, "Fabricante não encontrado");
                return;
            }
            String body = new String(exchange.getRequestBody().readAllBytes());
            Manufacture manufactureUpdate = convertFromJson(body, Manufacture.class);
            boolean result = manufactureRepository.update(manufactureUpdate, id);
            if (!result) {
                logger.info("Fabricante não encontrado id: " + id);
                sendResponse(exchange, 400, "Erro ao deletar Fabricante");
                return;
            }
            logger.info("Fabricante atualizado com sucesso");
            sendResponse(exchange, 201, "Fabricante atualizado com sucesso");
        } catch (Exception e) {
            logger.info("Erro ao atualizar Fabricante: " + e.getMessage());
            sendResponse(exchange, 400, "Erro ao atualizar Fabricante: " + e.getMessage());
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
