package com.institutosermelhor.ManagerCore.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.institutosermelhor.ManagerCore.infra.exception.ConflictException;
import com.institutosermelhor.ManagerCore.infra.exception.NotFoundException;
import com.institutosermelhor.ManagerCore.models.entity.Project;
import com.institutosermelhor.ManagerCore.models.repository.ProjectRepository;

@Service
public class ProjectService {

  private final ProjectRepository repository;

  @Autowired
  public ProjectService(ProjectRepository repository) {
    this.repository = repository;
  }

  public Project saveProject(Project newProject) {
    Project project = repository.findByName(newProject.getName());
    if (project != null) {
      throw new ConflictException("Project name already registered!");
    }

    return repository.save(newProject);
  }

  public List<Project> getProjects() {
    return repository.findByIsEnabledTrue();
  }

  public Project findById(String projectId) {
    return repository.findById(projectId)
        .orElseThrow(() -> new NotFoundException("Project not found!"));
  }

  public Project updateProject(Project projectToUpdate) {
    Project project = repository.findByName(projectToUpdate.getName());

    project.setDescription(projectToUpdate.getDescription());
    project.setName(projectToUpdate.getName());

    return repository.save(project);
  }

  public void deleteProject(String projectId) {
    Project project = findById(projectId);
    project.setEnabled(false);
    repository.save(project);
  }
}
