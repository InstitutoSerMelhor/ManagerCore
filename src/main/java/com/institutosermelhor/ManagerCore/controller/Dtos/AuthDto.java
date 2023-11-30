package com.institutosermelhor.ManagerCore.controller.Dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record AuthDto(
    @NotEmpty
    @Email
    String email,
    @NotEmpty
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[@#]).{6,}$")
    String password) {

}
