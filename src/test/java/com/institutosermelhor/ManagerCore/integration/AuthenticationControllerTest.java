package com.institutosermelhor.ManagerCore.integration;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.institutosermelhor.ManagerCore.MongoDbTestcontainerConfigTest;
import com.institutosermelhor.ManagerCore.controller.Dtos.UserCreationDto;
import com.institutosermelhor.ManagerCore.service.TokenService;
import com.institutosermelhor.ManagerCore.service.UserService;


@SpringBootTest
@SpringJUnitConfig
@AutoConfigureMockMvc
class AuthenticationControllerTest extends MongoDbTestcontainerConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired 
    private UserService userService;

    @Autowired 
    private TokenService tokenService;

    @Test
    @DisplayName("Test if Admin is sigin up in aplication")
    @WithMockUser(authorities = {"ADMIN"})
    void testSaveAdmin() throws Exception{
        UserCreationDto newUserAdmin = new UserCreationDto("New Name", "new.email@gmail.com", "NewPass123@");

        mockMvc.perform(
            MockMvcRequestBuilders.post("/register/admin/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(newUserAdmin)))
        .andExpect(status().isCreated());
    }
}
