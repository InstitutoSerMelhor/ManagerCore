package com.institutosermelhor.ManagerCore.controller.Dtos;

import com.institutosermelhor.ManagerCore.models.entity.User;

public record UserCreationDto(String name, String email, String password) {

  public User toEntity() {
    return User.builder().name(name).email(email)
        .password(password).build();
  }
}
