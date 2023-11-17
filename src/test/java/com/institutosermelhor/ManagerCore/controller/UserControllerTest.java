package com.institutosermelhor.ManagerCore.controller;

import com.institutosermelhor.ManagerCore.TestManagerCoreApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest extends TestManagerCoreApplication {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testUserGetUsers() throws Exception {
        ResultActions response = this.mockMvc
                .perform(MockMvcRequestBuilders.post("/users"));

        response.andExpect(status().isOk());
    }
}