package com.institutosermelhor.ManagerCore.controller.Dtos;

import com.institutosermelhor.ManagerCore.infra.security.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserDto(
        String id,
        @NotEmpty
        @Size(min = 5, message = "Characters min 5")
        String name,
        @NotEmpty
        @Email
        String email,
        @NotEmpty
        Role role
) {}
