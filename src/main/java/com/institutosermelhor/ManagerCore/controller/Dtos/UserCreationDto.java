package com.institutosermelhor.ManagerCore.controller.Dtos;

import com.institutosermelhor.ManagerCore.models.entity.User;

public record UserCreationDto(String username, String email, String password) {

  public User toEntity() {
    return User.builder().username(username).email(email)
        .password(password).build();
  }
}
