package com.institutosermelhor.ManagerCore.service;

import com.institutosermelhor.ManagerCore.infra.exception.ConflictException;
import com.institutosermelhor.ManagerCore.infra.exception.NotFoundException;
import com.institutosermelhor.ManagerCore.infra.security.Role;
import com.institutosermelhor.ManagerCore.models.entity.Project;
import com.institutosermelhor.ManagerCore.models.entity.User;
import com.institutosermelhor.ManagerCore.models.repository.ProjectRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

  private final ProjectRepository repository;

  @Autowired
  public ProjectService(ProjectRepository repository) {
    this.repository = repository;
  }

  public Project save(Project newProject) {
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

  public void delete(String projectId) {
    Project project = findById(projectId);
    project.setEnabled(false);
    repository.save(project);
  }

  public void update(Project project) {
    Project projectUpdated = this.findById(project.getId());
    projectUpdated.setName(project.getName());
    projectUpdated.setDescription(projectUpdated.getDescription());
    projectUpdated.setArea(project.getArea());
    repository.save(projectUpdated);
  }
}
