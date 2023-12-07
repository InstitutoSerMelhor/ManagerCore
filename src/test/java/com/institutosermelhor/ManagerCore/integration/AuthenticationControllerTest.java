package com.institutosermelhor.ManagerCore.integration;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.OutputStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.institutosermelhor.ManagerCore.MongoDbTestcontainerConfigTest;
import com.institutosermelhor.ManagerCore.controller.Dtos.UserCreationDto;
import com.institutosermelhor.ManagerCore.models.entity.User;
import com.institutosermelhor.ManagerCore.models.repository.UserRepository;
import com.institutosermelhor.ManagerCore.service.TokenService;
import com.institutosermelhor.ManagerCore.service.UserService;



@SpringBootTest
@SpringJUnitConfig
@AutoConfigureMockMvc
class AuthenticationControllerTest extends MongoDbTestcontainerConfigTest {

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private MockMvc mockMvc;

    @Autowired 
    private UserService userService;

    @Autowired 
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    
    @AfterEach
    void cleanAll() {
        this.userRepository.deleteAll();
    }

    @Test
    @DisplayName("Test if Admin is sigin up in aplication")
    @WithMockUser(authorities = {"ADMIN"})
    void testSaveAdmin() throws Exception{
        UserCreationDto newUserAdmin1 = UserCreationDto.builder().name("new name").email("new.email@gmail.com").password("newPass123@").build();

        UserCreationDto newUserAdmin = new UserCreationDto("New Name", "new.email@gmail.com", "NewPass123@");

        // User userInserted = this.userService.saveAdmin(newUserAdmin.toEntity());

        mockMvc.perform(
            MockMvcRequestBuilders.post("/register/admin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(newUserAdmin)))
        .andExpect(status().isCreated())
        // .andExpect(jsonPath("$").value(newUserAdmin))
        // .andExpect(jsonPath("$.[0]").value(newUserAdmin.toEntity()))
        .andDo(MockMvcResultHandlers.print())
        .andReturn()
        ;
        // Mockito.verify(userService).saveAdmin(newUserAdmin.toEntity());

        User userCreated = userRepository.findAll().get(0);

        System.out.println("testando" + userCreated);

        Assertions.assertEquals(true, encoder.matches("NewPass123@", userCreated.getPassword()));
    }
}
