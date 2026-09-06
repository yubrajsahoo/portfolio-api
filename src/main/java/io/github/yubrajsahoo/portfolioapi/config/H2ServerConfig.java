package io.github.yubrajsahoo.portfolioapi.config;

import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.sql.SQLException;

/**
 * Configuration to start an H2 TCP Server for local development.
 * This allows external SQL clients (like DBeaver or IntelliJ) to connect 
 * to the embedded H2 database running inside Docker.
 */
@Configuration
@Profile("dev")
public class H2ServerConfig {

    /**
     * Starts the H2 TCP server on port 9092.
     * 
     * @return the H2 Server instance
     * @throws SQLException if the server fails to start
     */
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2TcpServer() throws SQLException {
        // -tcpAllowOthers allows connections from outside the Docker container
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
    }
}
