package com.digicore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync(proxyTargetClass=true)
@SpringBootApplication(exclude= {UserDetailsServiceAutoConfiguration.class, DataSourceAutoConfiguration.class})
public class OmniRootServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OmniRootServiceApplication.class, args);
	}

}
