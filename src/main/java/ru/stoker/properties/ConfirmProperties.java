package ru.stoker.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "confirm")
public class ConfirmProperties {

    private String linkTemplate;

    private Duration waitDuration;

}
