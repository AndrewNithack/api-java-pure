package org.trabalhos;

import java.io.IOException;
import java.sql.SQLException;
import org.trabalhos.application.config.HttpConfig;
import org.trabalhos.application.controller.ManufactureHandler;
import org.trabalhos.application.controller.PartHandler;
import org.trabalhos.application.controller.UserHandler;
import org.trabalhos.infraestructure.repository.ManufactureRepository;
import org.trabalhos.infraestructure.repository.PartRepository;
import org.trabalhos.infraestructure.repository.UserRepository;

import static org.trabalhos.infraestructure.config.OracleConfig.getConnection;

public class Main {
    public static void main(String[] args) throws IOException, SQLException {
        HttpConfig server = new HttpConfig(8080);

        UserRepository userRepository = new UserRepository(getConnection());
        UserHandler userHandler = new UserHandler(userRepository);

        PartRepository partRepository = new PartRepository(getConnection());
        PartHandler partHandler = new PartHandler(partRepository);

        ManufactureRepository manufactureRepository = new ManufactureRepository(getConnection());
        ManufactureHandler manufactureHandler = new ManufactureHandler(manufactureRepository);

        server.registerRoutes("/users", userHandler);
        server.registerRoutes("/parts", partHandler);
        server.registerRoutes("/manufactures", manufactureHandler);

        server.start();
    }
}