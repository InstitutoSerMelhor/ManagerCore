package com.institutosermelhor.ManagerCore;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(
    description = "OpenApi documentation for Instituto Ser Melhor API",
    title = "Instituto Ser Melhor API",
    version = "1.0"
))
public class ManagerCoreApplication {

  public static void main(String[] args) {
    SpringApplication.run(ManagerCoreApplication.class, args);
  }

}
