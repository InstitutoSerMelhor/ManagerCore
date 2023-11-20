package com.institutosermelhor.ManagerCore.controller.Dtos;

import com.institutosermelhor.ManagerCore.models.entity.Project;

public record ProjectDto(String id, String name, String description, String area) {

  public Project toEntity() {
    return Project.builder().id(id).name(name).description(description)
        .area(area).build();
  }
}
