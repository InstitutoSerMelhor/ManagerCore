package com.institutosermelhor.ManagerCore.controller.Dtos;

import com.institutosermelhor.ManagerCore.infra.security.Role;

public record UserDto(String id, String username, String email, Role role) {

}
