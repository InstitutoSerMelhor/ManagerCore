package com.institutosermelhor.ManagerCore.controller;

import com.institutosermelhor.ManagerCore.MongoDbTestContainerConfigTest;
import com.institutosermelhor.ManagerCore.infra.security.Role;
import com.institutosermelhor.ManagerCore.models.entity.User;
import com.institutosermelhor.ManagerCore.models.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@SpringJUnitConfig
@AutoConfigureMockMvc
class UserControllerTest extends MongoDbTestContainerConfigTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void cleanAll() {
        this.userRepository.deleteAll();
    }

    @Test
    @DisplayName("1 - getUsers method when user collection is empty return an empty list")
    @WithMockUser
    void testApiEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(equalTo(List.of())))
                .andExpect(jsonPath("$.length()", is(0)));
    }

    @Test
    @DisplayName("2 - getUsers method when user collection has users return an users list")
    @WithMockUser
    void testApiEndpoint2() throws Exception {
        User user = this.userRepository.save(this.giveMeAnUser());
        String passwordHashed = new BCryptPasswordEncoder().encode("odeSaiDoMeuSofa");
        user.setPassword(passwordHashed);

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").isNotEmpty())
                .andExpect(jsonPath("$[0].name").value(giveMeAnUser().getName()))
                .andExpect(jsonPath("$[0].email").value(giveMeAnUser().getEmail()))
                .andExpect(jsonPath("$[0].role").value(giveMeAnUser().getRole().toString()));
    }

    private User giveMeAnUser() {
        String passwordHashed = new BCryptPasswordEncoder().encode("odeSaiDoMeuSofa");
        return new User(
                null,
                "Garfield",
                "lasanha@gmail.com",
                passwordHashed,
                Role.ADMIN,
                true,
                null,
                null);
    }
}