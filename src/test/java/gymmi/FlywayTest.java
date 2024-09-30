package gymmi;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("flyway")
@Testcontainers
public class FlywayTest {

    static String FLYWAY = "flyway";

    @Container
    static MySQLContainer mySQLContainer = new MySQLContainer<>("mysql:8.4.0")
            .withDatabaseName(FLYWAY)
            .withUsername(FLYWAY)
            .withPassword(FLYWAY);

    @Test
    void flyway() {
        Flyway flyway = Flyway.configure()
                .dataSource(mySQLContainer.getJdbcUrl(), FLYWAY, FLYWAY)
                .load();
        flyway.migrate();
    }
}
