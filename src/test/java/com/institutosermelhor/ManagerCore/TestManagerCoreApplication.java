package com.institutosermelhor.ManagerCore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestManagerCoreApplication {

	public static void main(String[] args) {
		SpringApplication.from(ManagerCoreApplication::main).with(TestManagerCoreApplication.class).run(args);
	}

}
