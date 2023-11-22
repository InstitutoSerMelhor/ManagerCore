package com.institutosermelhor.ManagerCore.controller.Dtos;

import com.institutosermelhor.ManagerCore.models.entity.User;

public record UserRequestDto(String id, String name, String email, String password) {

  public User toEntity() {
    return User.builder().id(id).name(name).email(email)
        .password(password).build();
  }
}
