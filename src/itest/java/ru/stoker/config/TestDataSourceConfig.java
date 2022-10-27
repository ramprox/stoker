package ru.stoker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import ru.stoker.database.entity.Advertisement;
import ru.stoker.database.entity.Attachment;
import ru.stoker.database.entity.Category;
import ru.stoker.database.entity.User;
import ru.stoker.util.DatabaseFacade;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

@TestConfiguration
@Import({TestContainersConfig.class, DataSourceConfig.class})
public class TestDataSourceConfig {

    private final TestContainersConfig testContainersConfig;

    private final DataSourceConfig dataSourceConfig;

    @Autowired
    public TestDataSourceConfig(TestContainersConfig testContainersConfig,
                                DataSourceConfig dataSourceConfig) {
        this.testContainersConfig = testContainersConfig;
        this.dataSourceConfig = dataSourceConfig;
    }

    @Bean
    public DatabaseFacade databaseCleaner(PlatformTransactionManager manager,
                                          TestEntityManager entityManager,
                                          List<String> tables,
                                          List<String> sequences) {
        DatabaseFacade databaseFacade = new DatabaseFacade(new TransactionTemplate(manager), entityManager);
        databaseFacade.addTables(tables);
        databaseFacade.addSequences(sequences);
        return databaseFacade;
    }

    @Bean
    public List<String> tables() {
        return List.of(
                Advertisement.TABLE_NAME,
                Category.TABLE_NAME,
                User.TABLE_NAME);
    }

    @Bean
    public List<String> sequences() {
        return List.of(
                Advertisement.SEQUENCE_NAME,
                Category.SEQUENCE_NAME,
                User.SEQUENCE_NAME,
                Attachment.SEQUENCE_NAME);
    }

    @Bean
    public DataSource dataSource() {
        DataSourceBuilder<?> builder = DataSourceBuilder.create()
                .driverClassName(dataSourceConfig.getDriverClassName());
        try {
            return connectToStartedDbServer(builder);
        } catch (Exception ex) {
            return startTestContainer(builder);
        }
    }

    private DataSource connectToStartedDbServer(DataSourceBuilder<?> builder) throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(
                testContainersConfig.getHost(),
                testContainersConfig.getPort()));
        socket.close();
        return builder
                .username(dataSourceConfig.getUsername())
                .password(dataSourceConfig.getPassword())
                .url(dataSourceConfig.getUrl())
                .build();
    }

    private DataSource startTestContainer(DataSourceBuilder<?> builder) {
        PostgreSQLContainer<?> container = new PostgreSQLContainer<>(DockerImageName.parse(testContainersConfig.getImageName()))
                .withDatabaseName(testContainersConfig.getDbName())
                .withUsername(dataSourceConfig.getUsername())
                .withPassword(dataSourceConfig.getPassword())
                .withEnv("POSTGRES_PASSWORD", dataSourceConfig.getPassword())
                .withExposedPorts(testContainersConfig.getPort());
        container.start();
        return builder.username(container.getUsername())
                .password(container.getPassword())
                .url(container.getJdbcUrl())
                .build();
    }

}
