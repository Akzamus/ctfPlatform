package com.cycnet.ctfPlatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.cycnet.ctfPlatform.configurations.properties")
public class CtfPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(CtfPlatformApplication.class, args);
	}

}
