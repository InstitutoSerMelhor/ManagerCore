package com.institutosermelhor.ManagerCore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.mongock.runner.springboot.EnableMongock;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@EnableMongock
@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(description = "OpenApi documentation for Instituto Ser Melhor Manager API",
        title = "Instituto Ser Melhor - API", version = "1.0"))
@SecurityScheme(name = "bearerAuth", description = "JWT auth required", scheme = "bearer",
    type = SecuritySchemeType.HTTP, bearerFormat = "JWT", in = SecuritySchemeIn.HEADER)
public class ManagerCoreApplication {

  public static void main(String[] args) {
    SpringApplication.run(ManagerCoreApplication.class, args);
  }

}
