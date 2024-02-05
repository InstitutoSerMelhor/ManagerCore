package com.institutosermelhor.ManagerCore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(description = "OpenApi documentation for Instituto Ser Melhor Manager API",
        title = "Instituto Ser Melhor - API", version = "1.0"))
@SecurityScheme(name = "bearerAuth", description = "JWT auth required", scheme = "bearer",
    type = SecuritySchemeType.HTTP, bearerFormat = "JWT", in = SecuritySchemeIn.HEADER)
public class ManagerCoreApplication extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(ManagerCoreApplication.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(ManagerCoreApplication.class);
  }
}
