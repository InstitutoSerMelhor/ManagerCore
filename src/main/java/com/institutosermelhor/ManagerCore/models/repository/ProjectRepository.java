package com.institutosermelhor.ManagerCore.models.repository;

import com.institutosermelhor.ManagerCore.models.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, String> {}
