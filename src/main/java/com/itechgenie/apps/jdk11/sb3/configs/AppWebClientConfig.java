package com.itechgenie.apps.jdk11.sb3.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "itg.webclient" )
@Data
public class AppWebClientConfig {

	private boolean debug = false;

}
