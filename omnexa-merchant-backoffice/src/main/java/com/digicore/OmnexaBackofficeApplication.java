package com.digicore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class OmnexaBackofficeApplication {

  public static void main(String[] args) {
    SpringApplication.run(OmnexaBackofficeApplication.class, args);
  }
}
