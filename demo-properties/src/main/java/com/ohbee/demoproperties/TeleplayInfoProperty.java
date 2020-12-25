package com.ohbee.demoproperties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@ConfigurationProperties("teleplay.info")
@Component
@Data
public class TeleplayInfoProperty {
    private String website;
    private Boolean enabled;
    private String title;
    private List<String> actors;
}
