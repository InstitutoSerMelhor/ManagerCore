package com.institutosermelhor.ManagerCore.unit.controller;

import com.institutosermelhor.ManagerCore.controller.AuthenticationController;
import com.institutosermelhor.ManagerCore.controller.Dtos.AuthDto;
import com.institutosermelhor.ManagerCore.controller.Dtos.AuthResponseDto;
import com.institutosermelhor.ManagerCore.controller.Dtos.UserCreationDto;
import com.institutosermelhor.ManagerCore.controller.Dtos.UserDto;
import com.institutosermelhor.ManagerCore.mocks.UserMock;
import com.institutosermelhor.ManagerCore.models.entity.User;
import com.institutosermelhor.ManagerCore.service.TokenService;
import com.institutosermelhor.ManagerCore.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthenticationController authenticationController;

    private final UserMock userMock = new UserMock();

    @Test
    @DisplayName("login method should return token")
    void testLogin() {
        String username = "user@example.com";
        String password = "password";
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        String validToken = "validToken";

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(new User());
        when(authenticationManager.authenticate(authToken)).thenReturn(auth);
        when(tokenService.generateToken(Mockito.any())).thenReturn(validToken);

        AuthDto authDto = new AuthDto(username, password);
        ResponseEntity<AuthResponseDto> responseEntity = authenticationController.login(authDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(validToken, responseEntity.getBody().token());
    }

    @Test
    @DisplayName("saveAdmin method should return token")
    void testSaveAdmin() {
        var mock = userMock.giveMeAnUser();
        when(userService.saveAdmin(Mockito.any())).thenReturn(mock);

        UserCreationDto userToCreate = new UserCreationDto(mock.getName(), mock.getEmail(), mock.getPassword());
        ResponseEntity<UserDto> responseUser = authenticationController.saveAdmin(userToCreate);

        assertEquals(responseUser.getStatusCode(), HttpStatus.CREATED);
        UserDto user = responseUser.getBody();
        assert user != null;
        assertEquals(user.id(), mock.getId());
        assertEquals(user.name(), mock.getName());
        assertEquals(user.email(), mock.getEmail());
        assertEquals(user.role(), mock.getRole());
    }

    @Test
    @DisplayName("saveUser method should return token")
    void testUser() {
        String validToken = "validToken";
        var mock = userMock.giveMeAnUser();
        when(userService.saveUser(Mockito.any())).thenReturn(mock);
        when(tokenService.generateToken(Mockito.any())).thenReturn(validToken);


        UserCreationDto userToCreate = new UserCreationDto(mock.getName(), mock.getEmail(), mock.getPassword());
        ResponseEntity<AuthResponseDto> responseUser = authenticationController.saveUser(userToCreate);

        assertEquals(responseUser.getStatusCode(), HttpStatus.CREATED);
        assertNotNull(responseUser.getBody());
        assertEquals(validToken, responseUser.getBody().token());
    }
}
