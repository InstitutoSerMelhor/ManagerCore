package com.institutosermelhor.ManagerCore.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.institutosermelhor.ManagerCore.MongoDbTestcontainerConfigTest;
import com.institutosermelhor.ManagerCore.controller.Dtos.ProjectCreationDto;
import com.institutosermelhor.ManagerCore.models.entity.Project;
import com.institutosermelhor.ManagerCore.service.ProjectService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@SpringJUnitConfig
@AutoConfigureMockMvc
class ProjectControllerTest extends MongoDbTestcontainerConfigTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ProjectService projectService;

  @Test
  @DisplayName("getProjects method when user collection is empty return an empty list")
  void testApiEndpoint() throws Exception {
    Mockito.when(projectService.getProjects()).thenReturn(List.of());

    mockMvc.perform(MockMvcRequestBuilders.get("/projects"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").value(equalTo(List.of())))
        .andExpect(jsonPath("$.length()", is(0)));
  }

  @Test
  @DisplayName("getProjects method when user collection has projects returns projects list")
  void testApiEndpoint2() throws Exception {
    Mockito.when(projectService.getProjects()).thenReturn(List.of(giveMeAProject()));

    mockMvc.perform(MockMvcRequestBuilders.get("/projects"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id").isNotEmpty())
        .andExpect(jsonPath("$[0].name").value(giveMeAProject().getName()))
        .andExpect(jsonPath("$[0].description").value(giveMeAProject().getDescription()))
        .andExpect(jsonPath("$[0].area").value(giveMeAProject().getArea()));

    Mockito.verify(projectService).getProjects();
  }

  @Test
  @DisplayName("delete method when delete a project returns no content")
  @WithMockUser(authorities = {"ADMIN"})
  void testApiEndpoint3() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/projects/" + giveMeAProject().getId()))
        .andExpect(status().isNoContent());

    Mockito.verify(projectService).delete(giveMeAProject().getId());
  }

  @Test
  @DisplayName("update method when update project data return project no content")
  @WithMockUser(authorities = {"ADMIN"})
  void testApiEndpoint5() throws Exception {
    ProjectCreationDto projectToUpdate = new ProjectCreationDto(
        "New project name",
        "New description",
        "New area"
    );

    mockMvc.perform(MockMvcRequestBuilders.put("/projects/" + giveMeAProject().getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(projectToUpdate)))
        .andExpect(status().isNoContent());

    Mockito.verify(projectService).update(giveMeAProject().getId(), projectToUpdate.toEntity());
  }

  @Test
  @DisplayName("getProject method when user collection has a project return this project")
  void testApiEndpoint6() throws Exception {
    Mockito.when(projectService.findById(eq(giveMeAProject().getId())))
        .thenReturn(giveMeAProject());

    mockMvc.perform(MockMvcRequestBuilders.get("/projects/" + giveMeAProject().getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.name").value(giveMeAProject().getName()))
        .andExpect(jsonPath("$.description").value(giveMeAProject().getDescription()))
        .andExpect(jsonPath("$.area").value(giveMeAProject().getArea()));
  }

  private Project giveMeAProject() {
    return new Project(
        "validId123",
        "Project 1",
        "Random project",
        "Development",
        null,
        null,
        true);
  }
}
