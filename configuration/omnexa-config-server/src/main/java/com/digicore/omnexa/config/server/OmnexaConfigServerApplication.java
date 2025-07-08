package com.digicore.omnexa.config.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class OmnexaConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(OmnexaConfigServerApplication.class, args);
	}

}
