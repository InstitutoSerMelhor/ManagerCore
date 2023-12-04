package com.institutosermelhor.ManagerCore.integration;

import com.institutosermelhor.ManagerCore.MongoDbTestcontainerConfigTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AvailabilityEndpointTest extends MongoDbTestcontainerConfigTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("endpoints that should be public(not throw unauthorized exception not matter if you're logged or not)")
    void testEndpoint() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/projects")).andExpect(status().is2xxSuccessful());
        mockMvc.perform(MockMvcRequestBuilders.get("/projects/fakeid11")).andExpect(status().is4xxClientError());
        mockMvc.perform(MockMvcRequestBuilders.get("/reports")).andExpect(status().is5xxServerError());
        mockMvc.perform(MockMvcRequestBuilders.get("/reports/fakeid11")).andExpect(status().is4xxClientError());
        mockMvc.perform(MockMvcRequestBuilders.post("/register")).andExpect(status().is4xxClientError());
        mockMvc.perform(MockMvcRequestBuilders.post("/login")).andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("endpoints that should be private(throw unauthorized exception if you're not logged)")
    void testEndpoint2() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/projects")).andExpect(status().is4xxClientError());
        mockMvc.perform(MockMvcRequestBuilders.delete("/projects/fakeid11")).andExpect(status().is4xxClientError());
        mockMvc.perform(MockMvcRequestBuilders.put("/projects/fakeid11")).andExpect(status().is4xxClientError());
        mockMvc.perform(MockMvcRequestBuilders.post("/reports")).andExpect(status().is4xxClientError());
        mockMvc.perform(MockMvcRequestBuilders.delete("/reports/fakeid11")).andExpect(status().is4xxClientError());
        mockMvc.perform(MockMvcRequestBuilders.put("/reports/fakeid11")).andExpect(status().is4xxClientError());
        mockMvc.perform(MockMvcRequestBuilders.put("/users/fakeid11")).andExpect(status().is4xxClientError());
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/fakeid11")).andExpect(status().is4xxClientError());
        mockMvc.perform(MockMvcRequestBuilders.get("/users/fakeid11")).andExpect(status().is4xxClientError());
        mockMvc.perform(MockMvcRequestBuilders.get("/users")).andExpect(status().is4xxClientError());
        mockMvc.perform(MockMvcRequestBuilders.post("/register/admin")).andExpect(status().is4xxClientError());
    }
}
