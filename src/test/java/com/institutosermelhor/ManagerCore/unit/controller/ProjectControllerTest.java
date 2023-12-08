package com.institutosermelhor.ManagerCore.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.institutosermelhor.ManagerCore.MongoDbTestcontainerConfigTest;
import com.institutosermelhor.ManagerCore.controller.Dtos.ProjectCreationDto;
import com.institutosermelhor.ManagerCore.mocks.ProjectMock;
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

  @Autowired
  private ProjectMock projectMock;

  @Test
  @DisplayName("getProjects method when project collection is empty return an empty list")
  void testApiEndpoint() throws Exception {
    Mockito.when(projectService.getProjects()).thenReturn(List.of());

    mockMvc.perform(MockMvcRequestBuilders.get("/projects"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").value(equalTo(List.of())))
        .andExpect(jsonPath("$.length()", is(0)));
  }

  @Test
  @DisplayName("getProjects method when project collection has projects returns projects list")
  void testApiEndpoint2() throws Exception {
    Mockito.when(projectService.getProjects()).thenReturn(List.of(projectMock.giveMeAProject()));

    mockMvc.perform(MockMvcRequestBuilders.get("/projects"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id").isNotEmpty())
        .andExpect(jsonPath("$[0].name").value(projectMock.giveMeAProject().getName()))
        .andExpect(
            jsonPath("$[0].description").value(projectMock.giveMeAProject().getDescription()))
        .andExpect(jsonPath("$[0].area").value(projectMock.giveMeAProject().getArea()));

    Mockito.verify(projectService).getProjects();
  }

  @Test
  @DisplayName("save method when creating a project returns the project created")
  @WithMockUser(authorities = {"ADMIN"})
  void testApiEndpoint3() throws Exception {
    Mockito.when(projectService.save((Project) any(Project.class)))
        .thenReturn(projectMock.giveMeAProject());

    mockMvc.perform(MockMvcRequestBuilders.get("/projects"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id").isNotEmpty())
        .andExpect(jsonPath("$[0].name").value(projectMock.giveMeAProject().getName()))
        .andExpect(
            jsonPath("$[0].description").value(projectMock.giveMeAProject().getDescription()))
        .andExpect(
            jsonPath("$[0].description").value(projectMock.giveMeAProject().getDescription()))
        .andExpect(jsonPath("$[0].area").value(projectMock.giveMeAProject().getArea()));

    Mockito.verify(projectService).getProjects();
  }

  @Test
  @DisplayName("delete method when delete a project returns no content")
  @WithMockUser(authorities = {"ADMIN"})
  void testApiEndpoint4() throws Exception {
    mockMvc.perform(
            MockMvcRequestBuilders.delete("/projects/" + projectMock.giveMeAProject().getId()))
        .andExpect(status().isNoContent());

    Mockito.verify(projectService).delete(projectMock.giveMeAProject().getId());
  }

  @Test
  @DisplayName("update method when update project data return project no content")
  @WithMockUser(authorities = {"ADMIN"})
  void testApiEndpoint5() throws Exception {
    ProjectCreationDto projectToUpdate = new ProjectCreationDto(
        "New project name",
        "A".repeat(200),
        "New area"
    );

    mockMvc.perform(MockMvcRequestBuilders.put("/projects/" + projectMock.giveMeAProject().getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(projectToUpdate)))
        .andExpect(status().isNoContent());

    Mockito.verify(projectService)
        .update(projectMock.giveMeAProject().getId(), projectToUpdate.toEntity());
  }

  @Test
  @DisplayName("getProject method when user collection has a project return this project")
  void testApiEndpoint6() throws Exception {
    Mockito.when(projectService.findById(eq(projectMock.giveMeAProject().getId())))
        .thenReturn(projectMock.giveMeAProject());

    mockMvc.perform(MockMvcRequestBuilders.get("/projects/" + projectMock.giveMeAProject().getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.name").value(projectMock.giveMeAProject().getName()))
        .andExpect(jsonPath("$.description").value(projectMock.giveMeAProject().getDescription()))
        .andExpect(jsonPath("$.area").value(projectMock.giveMeAProject().getArea()));
  }
}
