package com.institutosermelhor.ManagerCore.controller;

import com.institutosermelhor.ManagerCore.controller.Dtos.AuthenticationDto;
import com.institutosermelhor.ManagerCore.controller.Dtos.UserCreationDto;
import com.institutosermelhor.ManagerCore.controller.Dtos.UserDto;
import com.institutosermelhor.ManagerCore.models.entity.User;
import com.institutosermelhor.ManagerCore.service.TokenService;
import com.institutosermelhor.ManagerCore.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth")
public class AuthenticationController {

  private final AuthenticationManager authenticationManager;
  private final UserService userService;
  private final TokenService tokenService;

  @Autowired
  public AuthenticationController(AuthenticationManager authenticationManager,
      UserService userService, TokenService tokenService) {
    this.authenticationManager = authenticationManager;
    this.userService = userService;
    this.tokenService = tokenService;
  }

  @PostMapping("/register")
  public ResponseEntity<UserDto> create(@RequestBody UserCreationDto userData) {
    User newPerson = userService.create(userData.toEntity());

    UserDto userDto =
        new UserDto(newPerson.getUsername(), newPerson.getEmail(), newPerson.getRole());

    return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
  }

  @Secured("ADMIN")
  @SecurityRequirement(name = "bearerAuth")
  @PostMapping("/register/admin")
  public ResponseEntity<UserDto> createAdmin(@RequestBody UserCreationDto userData) {
    User newPerson = userService.createAdmin(userData.toEntity());

    UserDto userDto =
        new UserDto(newPerson.getUsername(), newPerson.getEmail(), newPerson.getRole());

    return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
  }

  @PostMapping("/login")
  public ResponseEntity<Map<String, String>> login(
      @RequestBody AuthenticationDto authenticationDTO) {
    UsernamePasswordAuthenticationToken usernamePassword =
        new UsernamePasswordAuthenticationToken(authenticationDTO.email(),
            authenticationDTO.password());
    Authentication auth = authenticationManager.authenticate(usernamePassword);
    User user = (User) auth.getPrincipal();
    String token = tokenService.generateToken(user);

    return ResponseEntity.status(HttpStatus.OK).body(Map.of("token", token));
  }

}
