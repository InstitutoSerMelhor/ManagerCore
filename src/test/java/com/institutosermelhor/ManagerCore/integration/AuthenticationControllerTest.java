package com.institutosermelhor.ManagerCore.integration;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.institutosermelhor.ManagerCore.MongoDbTestcontainerConfigTest;
import com.institutosermelhor.ManagerCore.controller.Dtos.UserCreationDto;
import com.institutosermelhor.ManagerCore.models.entity.User;
import com.institutosermelhor.ManagerCore.models.repository.UserRepository;
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
    private UserRepository userRepository;

    
    @AfterEach
    void cleanAll() {
        this.userRepository.deleteAll();
    }

    @Test
    @DisplayName("Test if Admin is sigin up in aplication")
    @WithMockUser(authorities = {"ADMIN"})
    void testSaveAdmin() throws Exception{
        UserCreationDto newUserAdmin = UserCreationDto.builder().name("new name").email("new.email@gmail.com").password("newPass123@").build();

        // Verify end point user admin:
        mockMvc.perform(
            MockMvcRequestBuilders.post("/register/admin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(newUserAdmin)))
        .andExpect(status().isCreated())
        // .andExpect(jsonPath("$").value(newUserAdmin))
        .andExpect(jsonPath("$").isNotEmpty())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.name").isNotEmpty())
        .andExpect(jsonPath("$.email").isNotEmpty())
        .andExpect(jsonPath("$.role").isNotEmpty())
        .andReturn();
        
        Mockito.verify(userService).saveAdmin(newUserAdmin.toEntity());

        //Verify if user admin is registered correcly:
        User userCreated = userRepository.findAll().get(0);

        System.out.println("testando" + userCreated);

        // Assertions.assertEquals("ROLE", userCreated.getRole());
        Assertions.assertEquals(true, encoder.matches("NewPass123@", userCreated.getPassword()));
        Assertions.assertEquals("new name", userCreated.getName());
        Assertions.assertEquals("new.email@gmail.com", userCreated.getEmail());
    }

    @Test
    @DisplayName("Test if Admin endpoint conflits with another registered email")
    @WithMockUser(authorities = {"ADMIN"})
    void testSaveAdminErro() throws Exception{
        UserCreationDto newUserAdmin = new UserCreationDto("New Name", "new.email@gmail.com", "NewPass123@");

        this.userService.saveAdmin(newUserAdmin.toEntity());

        mockMvc.perform(
            MockMvcRequestBuilders.post("/register/admin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(newUserAdmin)))
        .andExpect(status().is4xxClientError())
        .andExpect(jsonPath("$.message").isNotEmpty())
        .andExpect(jsonPath("$.message").value(is("Email already registered!")));
    }
}
