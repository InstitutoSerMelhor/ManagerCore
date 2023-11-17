package com.institutosermelhor.ManagerCore.controller;

import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.institutosermelhor.ManagerCore.infra.security.Role;
import com.institutosermelhor.ManagerCore.models.entity.User;
import com.institutosermelhor.ManagerCore.service.UserService;

@ContextConfiguration(classes = {UserController.class})
@ExtendWith(SpringExtension.class)
@Disabled
class UserControllerDiffblueTest {
  @Autowired
  private UserController userController;

  @MockBean
  private UserService userService;

  /**
     * Method under test: {@link UserController#getUsers()}
     */
    @Test
    void testGetUsers() throws Exception {
        when(userService.getUsers()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users");
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

  /**
   * Method under test: {@link UserController#getUsers()}
   */
  @Test
  void testGetUsers2() throws Exception {
    ArrayList<User> userList = new ArrayList<>();
    User.UserBuilder builderResult = User.builder();
    User.UserBuilder roleResult = builderResult
        .createdAt(
            Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()))
        .email("jane.doe@example.org").id("42").password("iloveyou").role(Role.ADMIN);
    User buildResult = roleResult
        .updatedAt(
            Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()))
        .username("janedoe").build();
    userList.add(buildResult);
    when(userService.getUsers()).thenReturn(userList);
    MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/users");
    MockMvcBuilders.standaloneSetup(userController).build().perform(requestBuilder)
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
        .andExpect(MockMvcResultMatchers.content().string(
            "[{\"id\":\"42\",\"username\":\"jane.doe@example.org\",\"email\":\"jane.doe@example.org\",\"role\":\"ADMIN\"}]"));
  }
}
