package com.institutosermelhor.ManagerCore.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.institutosermelhor.ManagerCore.MongoDbTestcontainerConfigTest;
import com.institutosermelhor.ManagerCore.controller.Dtos.ProjectCreationDto;
import com.institutosermelhor.ManagerCore.mocks.ProjectMock;
import com.institutosermelhor.ManagerCore.models.entity.Project;
import com.institutosermelhor.ManagerCore.models.repository.ProjectRepository;
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
class ProjectIntegrationTest extends MongoDbTestcontainerConfigTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ProjectRepository projectRepository;

  @Autowired
  private ProjectMock projectMock;

  @AfterEach
  void cleanAll() {
    this.projectRepository.deleteAll();
  }

  @Test
  @DisplayName("getProjects method when project collection is empty return an empty list")
  void testApiEndpoint() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/projects"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").value(equalTo(List.of())))
        .andExpect(jsonPath("$.length()", is(0)));
  }

  @Test
  @DisplayName("getProjects method when project collection has projects return an projects list")
  void testApiEndpoint2() throws Exception {
    this.projectRepository.save(this.projectMock.giveMeAProject());

    mockMvc.perform(MockMvcRequestBuilders.get("/projects"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id").isNotEmpty())
        .andExpect(jsonPath("$[0].name").value(this.projectMock.giveMeAProject().getName()))
        .andExpect(
            jsonPath("$[0].description").value(this.projectMock.giveMeAProject().getDescription()))
        .andExpect(jsonPath("$[0].area").value(this.projectMock.giveMeAProject().getArea()));
  }

  @Test
  @DisplayName("delete method when delete an project return no content")
  @WithMockUser(authorities = {"ADMIN"})
  void testApiEndpoint3() throws Exception {
    Project project = this.projectRepository.save(this.projectMock.giveMeAProject());

    mockMvc.perform(MockMvcRequestBuilders.delete("/projects/" + project.getId()))
        .andExpect(status().isNoContent());

    Project projectSearched = this.projectRepository.findAll().get(0);
    Assertions.assertFalse(projectSearched.isEnabled());
  }

  @Test
  @DisplayName("update method when update project data return project data updated")
  @WithMockUser(authorities = {"ADMIN"})
  void testApiEndpoint5() throws Exception {
    Project projectToCreate = this.projectMock.giveMeAProject();
    Project projectInserted = this.projectRepository.save(projectToCreate);

    ProjectCreationDto projectToUpdate = new ProjectCreationDto(
        "New project name",
        "A".repeat(200),
        "New area"
    );

    mockMvc.perform(MockMvcRequestBuilders.put("/projects/" + projectInserted.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(projectToUpdate)))
        .andExpect(status().isNoContent());

    Project projectUpdated = this.projectRepository.findAll().get(0);
    Assertions.assertEquals(projectUpdated.getId(), projectInserted.getId());
    Assertions.assertEquals(projectUpdated.getArea(), projectToUpdate.area());
    Assertions.assertEquals(projectUpdated.getDescription(), projectToUpdate.description());
    Assertions.assertEquals(projectUpdated.getName(), projectToUpdate.name());
  }

  @Test
  @DisplayName("getProject method when user collection has an user return this user")
  void testApiEndpoint6() throws Exception {
    Project project = this.projectRepository.save(this.projectMock.giveMeAProject());

    mockMvc.perform(MockMvcRequestBuilders.get("/projects/" + project.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.name").value(this.projectMock.giveMeAProject().getName()))
        .andExpect(
            jsonPath("$.description").value(this.projectMock.giveMeAProject().getDescription()))
        .andExpect(jsonPath("$.area").value(this.projectMock.giveMeAProject().getArea()));
  }
}
