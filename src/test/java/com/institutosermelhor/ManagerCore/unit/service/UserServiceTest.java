package com.institutosermelhor.ManagerCore.unit.service;

import com.institutosermelhor.ManagerCore.infra.exception.ConflictException;
import com.institutosermelhor.ManagerCore.infra.exception.NotFoundException;
import com.institutosermelhor.ManagerCore.infra.exception.UnauthorizedException;
import com.institutosermelhor.ManagerCore.mocks.UserMock;
import com.institutosermelhor.ManagerCore.models.entity.User;
import com.institutosermelhor.ManagerCore.models.repository.UserRepository;
import com.institutosermelhor.ManagerCore.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserMock userMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userMock = new UserMock();
    }

    @Test
    @DisplayName("saveUser method should save user successfully")
    void testSaveUserSuccessfully() {
        when(userRepository.findByEmail(any())).thenReturn(null);

        User userToSave = userMock.giveMeAnUserResponse();
        when(userRepository.save(any(User.class))).thenReturn(userToSave);

        User user = userService.saveUser(userToSave);

        assertEquals(user.getId(), userToSave.getId());
        assertEquals(user.getName(), userToSave.getName());
        assertEquals(user.getEmail(), userToSave.getEmail());
        assertEquals(user.getRole(), userToSave.getRole());
    }

    @Test
    @DisplayName("saveUser method should throw ConflictException if user with the same email already exists")
    void testSaveUserWithExistingEmail() {
        when(userRepository.findByEmail(any())).thenReturn(new User());

        assertThrows(ConflictException.class, () -> {
            User userToSave = userMock.giveMeAnUserResponse();
            userService.saveUser(userToSave);
        });
    }

    @Test
    @DisplayName("saveAdmin method should save user successfully")
    void testSaveAdminSuccessfully() {
        when(userRepository.findByEmail(any())).thenReturn(null);

        User userToSave = userMock.giveMeAnUserResponse();
        when(userRepository.save(any(User.class))).thenReturn(userToSave);

        User user = userService.saveAdmin(userToSave);

        assertEquals(user.getId(), userToSave.getId());
        assertEquals(user.getName(), userToSave.getName());
        assertEquals(user.getEmail(), userToSave.getEmail());
        assertEquals(user.getRole(), userToSave.getRole());
    }

    @Test
    @DisplayName("saveAdmin method should throw ConflictException if user with the same email already exists")
    void testSaveAdminWithExistingEmail() {
        when(userRepository.findByEmail(any())).thenReturn(new User());

        assertThrows(ConflictException.class, () -> {
            User userToSave = userMock.giveMeAnUserResponse();
            userService.saveAdmin(userToSave);
        });
    }

    @Test
    @DisplayName("findById method should get user successfully")
    void testFindByIdSuccessfully() {
        User user = userMock.giveMeAnUserResponse();
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(user));

        User userFinded = userService.findById(user.getId());

        assertEquals(user.getId(), userFinded.getId());
        assertEquals(user.getName(), userFinded.getName());
        assertEquals(user.getEmail(), userFinded.getEmail());
        assertEquals(user.getRole(), userFinded.getRole());
    }

    @Test
    @DisplayName("findById method should throw NotFoundException if user with the same id do not exists")
    void testFindByIdWithNoExistingUser() {
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            userService.findById("invalidid11");
        });
    }

    @Test
    @DisplayName("loadUserByUsername method should get user successfully")
    void testLoadUserByUsernameSuccessfully() {
        User user = userMock.giveMeAnUserResponse();
        when(userRepository.findByEmail(any())).thenReturn(user);

        UserDetails userFinded = userService.loadUserByUsername(user.getEmail());

        assertEquals(user.getEmail(), userFinded.getUsername());
        assertEquals(user.getPassword(), userFinded.getPassword());
    }

    @Test
    @DisplayName("loadUserByUsername method should throw UsernameNotFoundException if user with the same email do not exists")
    void testLoadUserByUsernameWithNoExistingUser() {
        when(userRepository.findByEmail(any())).thenThrow(UsernameNotFoundException.class);

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("invalidemail@bol.com");
        });
    }

    @Test
    @DisplayName("deleteUser service method should delete user successfully")
    void testDeleteSuccessfully() {
        User user = userMock.giveMeAnUserResponse();
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(user));

        assertDoesNotThrow(() -> userService.delete(user.getId(), user.getEmail()));
    }

    @Test
    @DisplayName("delete method should throw UnauthorizedException if user with the same email do not exists")
    void testDeleteWithNoExistingUser() {
        when(userRepository.findById(any())).thenThrow(UnauthorizedException.class);

        assertThrows(UnauthorizedException.class, () -> {
            userService.delete("invalidid","invalidemail@bol.com");
        });
    }

    @Test
    @DisplayName("update service method should delete user successfully")
    void testUpdateSuccessfully() {
        User user = userMock.giveMeAnUserResponse();
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(user));

        assertDoesNotThrow(() -> userService.update(user.getId(), user, user.getEmail()));
    }

    @Test
    @DisplayName("update method should throw UnauthorizedException if user with the same email do not exists")
    void testUpdateWithNoExistingUser() {
        when(userRepository.findById(any())).thenThrow(UnauthorizedException.class);

        assertThrows(UnauthorizedException.class, () -> {
            userService.update("invalidid", new User(),"invalidemail@bol.com");
        });
    }
}

