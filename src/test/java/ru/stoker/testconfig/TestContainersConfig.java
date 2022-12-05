package ru.stoker.testconfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;

@Getter
@Setter
@TestConfiguration
@ConfigurationProperties(prefix = "testcontainers")
public class TestContainersConfig {

    private String host;

    private int port;

    private String dbName;

    private String imageName;

}
