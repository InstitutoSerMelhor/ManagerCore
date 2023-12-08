package com.institutosermelhor.ManagerCore.unit.service;

import static org.mockito.ArgumentMatchers.eq;

import com.institutosermelhor.ManagerCore.infra.exception.ConflictException;
import com.institutosermelhor.ManagerCore.infra.exception.NotFoundException;
import com.institutosermelhor.ManagerCore.mocks.ProjectMock;
import com.institutosermelhor.ManagerCore.models.entity.Project;
import com.institutosermelhor.ManagerCore.models.repository.ProjectRepository;
import com.institutosermelhor.ManagerCore.service.ProjectService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@SpringJUnitConfig
public class ProjectServiceTest {

  @MockBean
  private ProjectRepository projectRepository;

  @Autowired
  private ProjectService projectService;

  @Autowired
  private ProjectMock projectMock;

  @Test
  @DisplayName("getProjects method should return a list of projects")
  void testApiEndpoint() {
    Mockito.when(projectRepository.findByIsEnabledTrue())
        .thenReturn(List.of(projectMock.giveMeAProject()));

    List<Project> projects = projectService.getProjects();

    assertEquals(projects.get(0).getId(), projectMock.giveMeAProject().getId());
    assertEquals(projects.get(0).getName(), projectMock.giveMeAProject().getName());
    assertEquals(projects.get(0).getArea(), projectMock.giveMeAProject().getArea());
    assertEquals(projects.get(0).getDescription(), projectMock.giveMeAProject().getDescription());
  }

  @Test
  @DisplayName("save method should return created project")
  void testApiEndpoint2() {
    Mockito.when(projectRepository.findByName("Project 1")).thenReturn(null);
    Mockito.when(projectRepository.save(any(Project.class)))
        .thenReturn(projectMock.giveMeAProject());

    Project response = projectService.save(projectMock.giveMeAProject());

    assertEquals(response.getId(), "validId123");
    assertEquals(response.getName(), "Project 1");
    assertEquals(response.getArea(), "Development");
    assertEquals(response.getDescription(), "Random project");
  }

  @Test
  @DisplayName("save method should throw an error if project name already exists on database")
  void testApiEndpoint3() {
    Mockito.when(projectRepository.findByName("Project 1"))
        .thenReturn(projectMock.giveMeAProject());

    assertThrows(ConflictException.class, () -> projectService.save(projectMock.giveMeAProject()));
  }

  @Test
  @DisplayName("delete method should return nothing")
  void testApiEndpoint4() {
    Mockito.when(projectRepository.findById("validId123")).thenReturn(
        Optional.ofNullable(projectMock.giveMeAProject()));

    projectService.delete("validId123");

    Mockito.verify(projectRepository).findById("validId123");
  }

  @Test
  @DisplayName("delete method should throw an error if project is not found")
  void testApiEndpoint5() {
    Mockito.when(projectRepository.findById("invalidId123")).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> projectService.delete("invalidId123"));
  }

  @Test
  @DisplayName("update method should return nothing")
  void testApiEndpoint6() {
    Mockito.when(projectRepository.findById("validId123")).thenReturn(
        Optional.ofNullable(projectMock.giveMeAProject()));

    projectService.update("validId123", projectMock.giveMeAProject());

    Mockito.verify(projectRepository).findById("validId123");
  }

  @Test
  @DisplayName("update method should throw an error if project is not found")
  void testApiEndpoint7() {
    Mockito.when(projectRepository.findById("invalidId123")).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class,
        () -> projectService.update("invalidId123", projectMock.giveMeAProject()));
  }
}
