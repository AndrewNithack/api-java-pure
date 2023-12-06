package org.trabalhos.infraestructure.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.trabalhos.application.model.User;

public class UserRepository {

    Logger logger = Logger.getLogger(getClass().getName());
    private final Connection connection;

    public UserRepository(Connection connection) {
        this.connection = connection;
    }

    public boolean insert(User user) {
        String sql = "INSERT INTO usuario (prenome, sobrenome, pwr) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.executeUpdate();
            logger.info("Usuário inserido com sucesso");
            return true;
        } catch (SQLException e) {
            logger.info("Erro ao inserir usuário: " + e.getMessage());
            return false;
        }
    }
    public boolean delete(String userId) {
        String sql = "DELETE FROM usuario WHERE lgn = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, userId);
            preparedStatement.executeUpdate();
            logger.info("Usuário deletado com sucesso");
            return true;
        } catch (SQLException e) {
            logger.info("Erro ao deletar usuário: " + e.getMessage());
            return false;
        }
    }

    public User getById(String id) {
        String sql = "SELECT * FROM usuario WHERE lgn = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mappingResponseToUser(resultSet);
            } else {
                logger.info("Usuário não encontrado");
                return null;
            }
        } catch (SQLException e) {
            logger.info("Erro ao buscar usuário: " + e.getMessage());
            return null;
        }
    }
    public List<User> list() {
        String sql = "SELECT * FROM usuario";
        List<User> result = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(
                        mappingResponseToUser(resultSet)
                );
            }
        } catch (SQLException e) {
            logger.info("Erro ao buscar usuário: " + e.getMessage());
            return List.of();
        }
        return result;
    }
    public boolean update(User user, String id) {
        String sql = "UPDATE usuario SET prenome = ?, sobrenome = ?, pwr = ? WHERE lgn = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setString(4, id);
            preparedStatement.executeUpdate();
            logger.info("Usuário alterado  com sucesso");
            return true;
        } catch (SQLException e) {
            logger.info("Erro ao alterar usuário: " + e.getMessage());
            return false;
        }
    }

    private User mappingResponseToUser(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getInt("lgn"),
                resultSet.getString("prenome"),
                resultSet.getString("sobrenome"),
                resultSet.getString("pwr")
        );
    }
}

