package com.institutosermelhor.ManagerCore.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.institutosermelhor.ManagerCore.MongoDbTestcontainerConfigTest;
import com.institutosermelhor.ManagerCore.controller.Dtos.UserCreationDto;
import com.institutosermelhor.ManagerCore.mocks.UserMock;
import com.institutosermelhor.ManagerCore.models.entity.User;
import com.institutosermelhor.ManagerCore.models.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
class UserIntegrationTest extends MongoDbTestcontainerConfigTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserMock userMock;

  @AfterEach
  void cleanAll() {
    this.userRepository.deleteAll();
  }

  @Test
  @DisplayName("getUsers method when user collection is empty return an empty list")
  @WithMockUser
  void testApiEndpoint() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").value(equalTo(List.of())))
        .andExpect(jsonPath("$.length()", is(0)));
  }

  @Test
  @DisplayName("getUsers method when user collection has users return an users list")
  @WithMockUser
  void testApiEndpoint2() throws Exception {
    User userMocked = this.userMock.giveMeAnUser();
    this.userRepository.save(userMocked);

    mockMvc.perform(MockMvcRequestBuilders.get("/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id").isNotEmpty())
        .andExpect(jsonPath("$[0].name").value(userMocked.getName()))
        .andExpect(jsonPath("$[0].email").value(userMocked.getEmail()))
        .andExpect(jsonPath("$[0].role").value(userMocked.getRole().toString()));
  }

  @Test
  @DisplayName("delete method when delete an user return no content")
  @WithMockUser(username = "lasanha@gmail.com")
  void testApiEndpoint3() throws Exception {
    User userMocked = this.userMock.giveMeAnUser();
    User user = this.userRepository.save(userMocked);

    mockMvc.perform(MockMvcRequestBuilders.delete("/users/" + user.getId()))
        .andExpect(status().isNoContent());

    User userSearched = this.userRepository.findAll().get(0);
    Assertions.assertFalse(userSearched.isEnabled());
  }

  @Test
  @DisplayName("update method when update user data return user data updated")
  @WithMockUser(username = "lasanha@gmail.com")
  void testApiEndpoint5() throws Exception {
    User userToCreate = this.userMock.giveMeAnUser();
    User userInserted = this.userRepository.save(userToCreate);

    UserCreationDto userToUpdate = UserCreationDto.builder()
        .name("Garfield Rei do Pedaço")
        .email("euamolasanha@gmail.com")
        .password("lasanhaLover")
        .build();

    mockMvc.perform(MockMvcRequestBuilders.put("/users/" + userInserted.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(userToUpdate)))
        .andExpect(status().isNoContent());

    User userUpdated = this.userRepository.findAll().get(0);
    Assertions.assertEquals(userUpdated.getId(), userInserted.getId());
    Assertions.assertNotEquals(userUpdated.getPassword(), userInserted.getPassword());
    Assertions.assertEquals(userUpdated.getEmail(), userToUpdate.email());
    Assertions.assertEquals(userUpdated.getName(), userToUpdate.name());
  }

  @Test
  @DisplayName("getUser method when user collection has an user return this user")
  @WithMockUser
  void testApiEndpoint6() throws Exception {
    User userMocked = this.userMock.giveMeAnUser();
    User user = this.userRepository.save(userMocked);

    mockMvc.perform(MockMvcRequestBuilders.get("/users/" + user.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.name").value(userMocked.getName()))
        .andExpect(jsonPath("$.email").value(userMocked.getEmail()))
        .andExpect(jsonPath("$.role").value(userMocked.getRole().toString()));
  }

  @Test
  @DisplayName("getUser method when user collection is empty throw not found")
  @WithMockUser
  void testApiEndpoint7() throws Exception {
    String fakeId = "455555";

    mockMvc.perform(MockMvcRequestBuilders.get("/users/" + fakeId))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(is("User not found!")));
  }
}