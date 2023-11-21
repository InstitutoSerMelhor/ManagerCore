package com.institutosermelhor.ManagerCore;

import jakarta.validation.constraints.AssertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
@Testcontainers
@TestPropertySource(properties = {
        "spring.data.mongodb.uri=mongodb://${testcontainer.container.host}:${testcontainer.container.port}/${testcontainer.container.databaseName}"
})
public class MongoDbTestContainerConfigTest {
    @Container
    public static MongoDBContainer mongoDBContainer = new MongoDBContainer(
                DockerImageName.parse("mongo:latest"))
            .withExposedPorts(27017)
            .withEnv("MONGO_INITDB_DATABASE", "testdb");

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("testcontainer.container.host", mongoDBContainer::getHost);
        registry.add("testcontainer.container.port", mongoDBContainer::getFirstMappedPort);
    }

    @Test
    @DisplayName("3 - Test container is healthy")
    void testContainerisHealthy() {
        assert mongoDBContainer.isCreated();
        assert mongoDBContainer.isRunning();
    }
}
