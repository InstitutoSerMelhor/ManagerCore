package com.institutosermelhor.ManagerCore;

import com.institutosermelhor.ManagerCore.mocks.ProjectMock;
import com.institutosermelhor.ManagerCore.mocks.UserMock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfiguration {
    @Bean
    public UserMock getUser() {
        return new UserMock();
    }

    @Bean
    public ProjectMock getProject() {
        return new ProjectMock();
    }
}
