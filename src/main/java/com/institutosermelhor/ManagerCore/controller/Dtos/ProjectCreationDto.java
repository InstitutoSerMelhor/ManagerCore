package com.institutosermelhor.ManagerCore.controller.Dtos;

import com.institutosermelhor.ManagerCore.models.entity.Project;

public record ProjectCreationDto(String name, String description, String area) {

  public Project toEntity() {
    return Project.builder().name(name).description(description).area(area).build();
  }
}
