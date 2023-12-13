package com.institutosermelhor.ManagerCore.integration;

import static org.hamcrest.Matchers.is;
import static org.mockito.Answers.values;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.institutosermelhor.ManagerCore.MongoDbTestcontainerConfigTest;
import com.institutosermelhor.ManagerCore.controller.Dtos.AuthDto;
import com.institutosermelhor.ManagerCore.controller.Dtos.UserCreationDto;
import com.institutosermelhor.ManagerCore.infra.security.Role;
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

    @Autowired
    private AuthenticationManager authenticationManager;


    
    @AfterEach
    void cleanAll() {
        this.userRepository.deleteAll();
    }

    @Test
    @DisplayName("Testing endpoint and if Admin is sigin up in aplication and his return is created and your values")
    @WithMockUser(authorities = {"ADMIN"})
    void testSaveAdmin() throws Exception{
        final String name = "New Name";
        final String email = "new.email@gmail.com";
        final String password = "NewPass123@";

        UserCreationDto newUserAdmin = new UserCreationDto(name, email, password);

        // Verify end point user admin:
        mockMvc.perform(
            MockMvcRequestBuilders.post("/register/admin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(newUserAdmin)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$").isNotEmpty())
            .andExpect(jsonPath("$.id").isNotEmpty())
            .andExpect(jsonPath("$.name").isNotEmpty())
            .andExpect(jsonPath("$.email").isNotEmpty())
            .andExpect(jsonPath("$.role").isNotEmpty())
            .andReturn();
        
        //Verify if user admin is registered correcly:
        User userCreated = userRepository.findAll().get(0);

        Matcher matcherId = Pattern.compile("[a-z0-9]{24}").matcher(userCreated.getId());
        Matcher matcherPassEncode = Pattern.compile("\\A\\$2(a|y|b)?\\$(\\d\\d)\\$[./0-9A-Za-z]{53}").matcher(userCreated.getPassword());

        Assertions.assertNotNull(userCreated.getId());
        Assertions.assertEquals(true, matcherId.find());

        Assertions.assertEquals(Role.ADMIN, userCreated.getRole());

        Assertions.assertEquals(true, encoder.matches(password, userCreated.getPassword()));
        Assertions.assertEquals(true, matcherPassEncode.find());

        Assertions.assertEquals(name, userCreated.getName());
        Assertions.assertEquals(email, userCreated.getEmail());
    }

    @Test
    @DisplayName("Test if Admin endpoint conflits with another registered email, returning client error (400)")
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

    @Test
    @DisplayName("Test if normal user try use endpoint save admin and his return is erro client (400)")
    @WithMockUser(authorities = {"USER"})
    void testBlockUserRole() throws Exception{
        UserCreationDto newUserAdmin = new UserCreationDto("New Name", "new.email@gmail.com", "NewPass123@");

        mockMvc.perform(
            MockMvcRequestBuilders.post("/register/admin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(newUserAdmin)))
            .andExpect(status().is4xxClientError())
            .andExpect(header().string("X-Content-Type-Options", "nosniff"))
            .andExpect(header().string("X-Frame-Options", "DENY"))
            .andExpect(header().string("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate"))
            .andExpect(header().string("Pragma", "no-cache"))
            .andExpect(header().string("Expires", "0"))
            .andExpect(jsonPath("$").value(is("Acesso negado")));
    }

    @Test
    @DisplayName("Test if user admin try login in application and return status 204 and token value")
    @WithMockUser(authorities = {"ADIMN"})
    void testAdminLogin() throws Exception{
        final String email = "new.email@gmail.com";
        final String password = "NewPass123@";

        UserCreationDto newUserAdmin = new UserCreationDto("New Name", email, password);

        this.userService.saveAdmin(newUserAdmin.toEntity());

        AuthDto userLogin = new AuthDto(email, password);

        mockMvc.perform(
            MockMvcRequestBuilders.post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(userLogin))
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").isNotEmpty());
    }

    // @Test
    // @DisplayName("Test if user admin try login in application and return status 204 and token value")
    // @WithMockUser(authorities = {"ADIMN"})
    // void testAdminLogin() throws Exception{
    //     UserCreationDto newUserAdmin = new UserCreationDto("New Name", "new.email@gmail.com", "NewPass123@");

    //     User userInserted = this.userService.saveAdmin(newUserAdmin.toEntity());

    //     mockMvc.perform(
    //         MockMvcRequestBuilders.post("/login")
    //         .contentType(MediaType.APPLICATION_JSON)
    //         .content(new ObjectMapper().writeValueAsString(userInserted))
    //     )
    //     .andExpect(status().is4xxClientError())
    //     .andExpect(jsonPath("$.message").value(is("mensage")));
    // }
}
