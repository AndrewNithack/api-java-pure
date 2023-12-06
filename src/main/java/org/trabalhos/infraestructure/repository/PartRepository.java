package org.trabalhos.infraestructure.repository;

import org.trabalhos.application.model.Part;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static java.lang.Integer.parseInt;

public class PartRepository {

    Logger logger = Logger.getLogger(getClass().getName());
    private final Connection connection;

    public PartRepository(Connection connection) {
        this.connection = connection;
    }

    public boolean insert(Part part) {
        String sql = "INSERT INTO peca (nomePeca, preco, idFabricante) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, part.getName());
            preparedStatement.setDouble(2, part.getPrice());
            preparedStatement.setInt(3, part.getManufacturerId());
            preparedStatement.executeUpdate();
            logger.info("Peça inserida com sucesso");
            return true;
        } catch (SQLException e) {
            logger.info("Erro ao inserir Peça: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(String partId) {
        String sql = "DELETE FROM peca WHERE idPeca = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, partId);
            preparedStatement.executeUpdate();
            logger.info("Peça deletada com sucesso");
            return true;
        } catch (SQLException e) {
            logger.info("Erro ao deletar Peça: " + e.getMessage());
            return false;
        }
    }

    public Part getById(String id) {
        String sql = "SELECT * FROM peca WHERE idPeca = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mappingResponseToPart(resultSet);
            } else {
                logger.info("Peça não encontrada");
                return null;
            }
        } catch (SQLException e) {
            logger.info("Erro ao buscar Peça: " + e.getMessage());
            return null;
        }
    }

    public List<Part> list() {
        String sql = "SELECT * FROM peca";
        List<Part> result = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(
                        mappingResponseToPart(resultSet)
                );
            }
        } catch (SQLException e) {
            logger.info("Erro ao buscar Peça: " + e.getMessage());
            return List.of();
        }
        return result;
    }

    public boolean update(Part part, String id) {
        String sql = "UPDATE peca SET nomePeca = ?, preco = ?, idFabricante = ? WHERE idPeca = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, part.getName());
            preparedStatement.setDouble(2, part.getPrice());
            preparedStatement.setInt(3, part.getManufacturerId());
            preparedStatement.setString(4, id);
            preparedStatement.executeUpdate();
            logger.info("Peça alterada com sucesso");
            return true;
        } catch (SQLException e) {
            logger.info("Erro ao alterar Peça: " + e.getMessage());
            return false;
        }
    }

    private Part mappingResponseToPart(ResultSet resultSet) throws SQLException {
        return new Part(
                resultSet.getInt("idPeca"),
                resultSet.getString("nomePeca"),
                resultSet.getDouble("preco"),
                resultSet.getInt("idFabricante")
        );
    }
}
