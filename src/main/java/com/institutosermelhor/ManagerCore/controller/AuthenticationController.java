package com.institutosermelhor.ManagerCore.controller;

import com.institutosermelhor.ManagerCore.controller.Dtos.AuthDto;
import com.institutosermelhor.ManagerCore.controller.Dtos.AuthResponseDto;
import com.institutosermelhor.ManagerCore.controller.Dtos.UserCreationDto;
import com.institutosermelhor.ManagerCore.controller.Dtos.UserDto;
import com.institutosermelhor.ManagerCore.infra.exception.BadRequestException;
import com.institutosermelhor.ManagerCore.models.entity.User;
import com.institutosermelhor.ManagerCore.service.TokenService;
import com.institutosermelhor.ManagerCore.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@Tag(name = "Authentication")
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
  public ResponseEntity<AuthResponseDto> saveUser(@RequestBody @Valid UserCreationDto userData) {
    User newUser = userService.saveUser(userData.toEntity());
    AuthResponseDto token = new AuthResponseDto(tokenService.generateToken(newUser));
    return ResponseEntity.status(HttpStatus.CREATED).body(token);
  }

  @Secured("ADMIN")
  @SecurityRequirement(name = "bearerAuth")
  @PostMapping("/register/admin")
  public ResponseEntity<UserDto> saveAdmin(@RequestBody @Valid UserCreationDto userData) {
    User newUser = userService.saveAdmin(userData.toEntity());

    UserDto userDto =
        new UserDto(newUser.getId(), newUser.getName(), newUser.getEmail(), newUser.getRole());

    return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponseDto> login(@RequestBody @Valid AuthDto authenticationDTO) {
    try {
      var usernamePassword = new UsernamePasswordAuthenticationToken(
              authenticationDTO.email(),
              authenticationDTO.password());

      Authentication auth = authenticationManager.authenticate(usernamePassword);
      User user = (User) auth.getPrincipal();
      AuthResponseDto token = new AuthResponseDto(tokenService.generateToken(user));

      return ResponseEntity.status(HttpStatus.OK).body(token);
    } catch (InternalAuthenticationServiceException | BadCredentialsException e) {
      throw new BadRequestException("Email or password incorrect");
    }
  }
}
