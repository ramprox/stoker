package ru.stoker.testconfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;

@Setter
@Getter
@TestConfiguration
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceConfig {

    private String url;

    private String driverClassName;

    private String username;

    private String password;

}
