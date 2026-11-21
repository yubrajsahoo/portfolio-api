package io.github.yubrajsahoo.portfolioapi.unit.config;

import io.github.yubrajsahoo.portfolioapi.config.H2ServerConfig;
import org.h2.tools.Server;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Unit: H2 Server Configuration")
class H2ServerConfigTest {

    @Test
    @DisplayName("Should Create H2 TCP Server Bean")
    void h2TcpServer_CreatesServer() throws SQLException {
        H2ServerConfig config = new H2ServerConfig();
        Server server = config.h2TcpServer();
        assertNotNull(server);
    }
}
