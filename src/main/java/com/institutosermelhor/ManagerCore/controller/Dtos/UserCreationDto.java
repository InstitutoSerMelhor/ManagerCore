package com.institutosermelhor.ManagerCore.controller.Dtos;

import com.institutosermelhor.ManagerCore.models.entity.User;
import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record UserCreationDto(
        @NotEmpty
        @Size(min = 5, message = "Characters min 5")
        String name,
        @NotEmpty
        @Email
        String email,
        @NotEmpty
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[@#]).{6,}$")
        String password
) {

  public User toEntity() {
    return User.builder().name(name).email(email)
        .password(password).build();
  }
}
