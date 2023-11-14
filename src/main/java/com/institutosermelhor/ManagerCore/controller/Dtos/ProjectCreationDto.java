package com.institutosermelhor.ManagerCore.controller.Dtos;

import com.institutosermelhor.ManagerCore.models.entity.Project;

public record ProjectCreationDto(String name, String desciption) {

  public Project toEntity() {
    return Project.builder().name(name).description(desciption).build();
  }
}
