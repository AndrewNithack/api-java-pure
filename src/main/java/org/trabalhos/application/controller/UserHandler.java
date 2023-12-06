package org.trabalhos.application.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Logger;
import org.trabalhos.application.model.User;
import org.trabalhos.infraestructure.repository.UserRepository;

import static org.trabalhos.util.JsonConverterUtil.convertArrayToJson;
import static org.trabalhos.util.JsonConverterUtil.convertFromJson;
import static org.trabalhos.util.JsonConverterUtil.convertToJson;

public class UserHandler implements HttpHandler {

    UserRepository userRepository;
    public UserHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    Logger logger = Logger.getLogger(getClass().getName());
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        switch (method) {
            case "GET" -> {
                logger.info("GET");
                int validation = exchange.getRequestURI().getPath().split("/").length;
            if(validation == 2){
                listUsers(exchange);
            }
            if (validation == 3)
                getUser(exchange);
            }
            case "POST" -> {
                logger.info("POST");
                createUser(exchange);
            }
            case "PUT" -> {
                logger.info("PUT");
                updateUser(exchange);
            }
            case "DELETE" -> {
                logger.info("DELETE");
                deleteUser(exchange);

            }
            default -> {
                logger.info("Método não suportado: " + method);
                sendResponse(exchange, 405, "Método não suportado: " + method);
            }
        }
    }

    private void getUser(HttpExchange exchange) throws IOException {
        try {
            String id = exchange.getRequestURI().getPath().split("/")[2];
            User user = userRepository.getById(id);
            if(user == null) {
                logger.info("Usuário não encontrado");
                sendResponse(exchange, 404, "Usuário não encontrado");
                return;
            }
            String responseText = convertToJson(user);
            sendResponse(exchange, 200, responseText);
        } catch (Exception e) {
            logger.info("Erro ao buscar usuário: " + e.getMessage());
            sendResponse(exchange, 400, "Erro ao buscar usuário: " + e.getMessage());
        }
    }
    private void listUsers(HttpExchange exchange) throws  IOException {
        try {
            List<User> users = userRepository.list();
            if(users == null) {
                logger.info("Nenhum usuário encontrado");
                sendResponse(exchange, 404, "Nenhum usuário encontrado");
                return;
            }
            String responseText = convertArrayToJson(users);
            sendResponse(exchange, 200, responseText);

        }catch (Exception e) {
                logger.info("Erro ao buscar usuários: " + e.getMessage());
                sendResponse(exchange, 400, "Erro ao buscar usuários: " + e.getMessage());
            }

    }

    private void createUser(HttpExchange exchange) throws IOException {
        try {
            String body = new String(exchange.getRequestBody().readAllBytes());
            User user = convertFromJson(body, User.class);

            boolean result = userRepository.insert(user);
            if(!result) {
                logger.info("Erro ao inserir usuário");
                sendResponse(exchange, 400, "Erro ao inserir usuário");
                return;
            }
            logger.info("Usuário inserido com sucesso");
            sendResponse(exchange, 201, "Usuário inserido com sucesso");

        } catch (Exception e) {
            logger.info("Erro ao criar usuário: " + e.getMessage());
            sendResponse(exchange, 400, "Erro ao criar usuário: " + e.getMessage());
        }
    }
    private void deleteUser(HttpExchange exchange) throws IOException {
        try {
            String id = exchange.getRequestURI().getPath().split("/")[2];
            User user = userRepository.getById(id);
            if(user == null) {
                logger.info("Usuário não encontrado");
                sendResponse(exchange, 404, "Usuário não encontrado");
                return;
            }
            boolean result = userRepository.delete(id);
            if(!result) {
                logger.info("Usuário não encontrado id: " + id);
                sendResponse(exchange, 400, "Erro ao deletar usuário");
                return;
            }
            logger.info("Usuário deletado com sucesso");
            sendResponse(exchange, 201, "Usuário deletado com sucesso");

        } catch (Exception e) {
            logger.info("Erro ao deletar usuário: " + e.getMessage());
            sendResponse(exchange, 400, "Erro ao deletar usuário: " + e.getMessage());
        }
    }
    private void updateUser(HttpExchange exchange) throws IOException {
        try {
            String id = exchange.getRequestURI().getPath().split("/")[2];
            User user = userRepository.getById(id);
            if(user == null) {
                logger.info("Usuário não encontrado");
                sendResponse(exchange, 404, "Usuário não encontrado");
                return;
            }
            String body = new String(exchange.getRequestBody().readAllBytes());
            User userUpdate = convertFromJson(body, User.class);
            boolean result = userRepository.update(userUpdate, id);
            if(!result) {
                logger.info("Usuário não encontrado id: " + id);
                sendResponse(exchange, 400, "Erro ao deletar usuário");
                return;
            }

            logger.info("Usuário atualizado com sucesso");
            sendResponse(exchange, 201, "Usuário atualizado com sucesso");

        } catch (Exception e) {
            logger.info("Erro ao atualizar usuário: " + e.getMessage());
            sendResponse(exchange, 400, "Erro ao atualizar usuário: " + e.getMessage());
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



