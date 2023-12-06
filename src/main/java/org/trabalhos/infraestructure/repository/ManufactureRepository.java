package org.trabalhos.infraestructure.repository;

import org.trabalhos.application.model.Manufacture;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ManufactureRepository {

    Logger logger = Logger.getLogger(getClass().getName());
    private final Connection connection;

    public ManufactureRepository(Connection connection) {
        this.connection = connection;
    }

    public boolean insert(Manufacture manufacture) {
        String sql = "INSERT INTO FABRICANTE (NOMEFABRICANTE) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, manufacture.getName());
            preparedStatement.executeUpdate();
            logger.info("Fabricante inserido com sucesso");
            return true;
        } catch (SQLException e) {
            logger.warning("Erro ao inserir Fabricante: " + e.getMessage());
            return false;
        }
    }

    public boolean delete(String manufactureId) {
        String sql = "DELETE FROM FABRICANTE WHERE IDFABRICANTE = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, manufactureId);
            preparedStatement.executeUpdate();
            logger.info("Fabricante deletado com sucesso");
            return true;
        } catch (SQLException e) {
            logger.info("Erro ao deletar Fabricante: " + e.getMessage());
            return false;
        }
    }

    public Manufacture getById(String id) {
        String sql = "SELECT * FROM FABRICANTE WHERE IDFABRICANTE = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return mappingResponseToManufacture(resultSet);
            } else {
                logger.info("Fabricante não encontrado");
                return null;
            }
        } catch (SQLException e) {
            logger.info("Erro ao buscar Fabricante: " + e.getMessage());
            return null;
        }
    }

    public List<Manufacture> list() {
        String sql = "SELECT * FROM FABRICANTE";
        List<Manufacture> result = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                result.add(mappingResponseToManufacture(resultSet));
            }
        } catch (SQLException e) {
            logger.info("Erro ao buscar Fabricante: " + e.getMessage());
            return List.of();
        }
        return result;
    }

    public boolean update(Manufacture manufacture, String id) {
        String sql = "UPDATE FABRICANTE SET NOMEFABRICANTE = ? WHERE IDFABRICANTE = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, manufacture.getName());
            preparedStatement.setString(2, id);
            preparedStatement.executeUpdate();
            logger.info("Fabricante alterado com sucesso");
            return true;
        } catch (SQLException e) {
            logger.info("Erro ao alterar Fabricante: " + e.getMessage());
            return false;
        }
    }

    private Manufacture mappingResponseToManufacture(ResultSet resultSet) throws SQLException {
        return new Manufacture(
                resultSet.getInt("idFabricante"),
                resultSet.getString("nomeFabricante")
        );
    }
}
