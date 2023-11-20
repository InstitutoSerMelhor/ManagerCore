package com.institutosermelhor.ManagerCore.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.institutosermelhor.ManagerCore.controller.Dtos.ProjectCreationDto;
import com.institutosermelhor.ManagerCore.controller.Dtos.ProjectDto;
import com.institutosermelhor.ManagerCore.models.entity.Project;
import com.institutosermelhor.ManagerCore.service.ProjectService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/projects")
@Tag(name = "Projects")
public class ProjectController {

  private final ProjectService service;

  @Autowired
  public ProjectController(ProjectService service) {
    this.service = service;
  }

  @GetMapping()
  public ResponseEntity<List<ProjectDto>> getProjects() {
    List<Project> projects = service.getProjects();
    List<ProjectDto> projectsDto = projects.stream()
        .map(user -> new ProjectDto(user.getId(), user.getName(), user.getDescription())).toList();

    return ResponseEntity.ok(projectsDto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProjectDto> findById(@PathVariable String id) {
    Project project = service.findById(id);
    ProjectDto projectDto =
        new ProjectDto(project.getId(), project.getName(), project.getDescription());
    return ResponseEntity.ok(projectDto);
  }

  @PostMapping()
  public ResponseEntity<ProjectDto> saveProject(@RequestBody ProjectCreationDto newProject) {
    Project project = service.saveProject(newProject.toEntity());
    ProjectDto projectDto =
        new ProjectDto(project.getId(), project.getName(), project.getDescription());
    return ResponseEntity.status(HttpStatus.CREATED).body(projectDto);
  }

  @PutMapping()
  public ResponseEntity<ProjectDto> updateProject(@RequestBody ProjectCreationDto newProject) {
    Project project = service.updateProject(newProject.toEntity());
    ProjectDto projectDto =
        new ProjectDto(project.getId(), project.getName(), project.getDescription());
    return ResponseEntity.status(HttpStatus.OK).body(projectDto);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProject(@PathVariable String id) {
    service.deleteProject(id);
    return ResponseEntity.noContent().build();
  }
}
