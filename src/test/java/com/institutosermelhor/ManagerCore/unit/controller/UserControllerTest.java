    package com.institutosermelhor.ManagerCore.unit.controller;

    import com.institutosermelhor.ManagerCore.controller.Dtos.UserCreationDto;
    import com.institutosermelhor.ManagerCore.controller.Dtos.UserDto;
    import com.institutosermelhor.ManagerCore.controller.UserController;
    import com.institutosermelhor.ManagerCore.mocks.UserMock;
    import com.institutosermelhor.ManagerCore.models.entity.User;
    import com.institutosermelhor.ManagerCore.service.UserService;
    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.DisplayName;
    import org.junit.jupiter.api.Test;
    import org.mockito.InjectMocks;
    import org.mockito.Mock;
    import org.mockito.Mockito;
    import org.mockito.MockitoAnnotations;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.core.userdetails.UserDetails;

    import java.util.List;

    import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
    import static org.junit.jupiter.api.Assertions.assertEquals;
    import static org.mockito.Mockito.when;

    class UserControllerTest {

        private UserMock userMock;

        @Mock
        private UserService userService;

        @InjectMocks
        private UserController userController;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.initMocks(this);
            userMock = new UserMock();
        }

        @Test
        @DisplayName("getUsers method should return a list of UserDto when user collection is not empty")
        void testGetUsersNotEmpty() {
            User user = userMock.giveMeAnUserResponse();
            when(userService.getUsers()).thenReturn(List.of(user));

            ResponseEntity<List<UserDto>> responseEntity = userController.getUsers();
            List<UserDto> userDtos = responseEntity.getBody();

            assertEquals(1, userDtos.size());
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(user.getId(), userDtos.get(0).id());
            assertEquals(user.getName(), userDtos.get(0).name());
            assertEquals(user.getEmail(), userDtos.get(0).email());
            assertEquals(user.getRole(), userDtos.get(0).role());
        }

        @Test
        @DisplayName("getUsers method should return an empty list when user collection is empty")
        void testGetUsersEmpty() {
            when(userService.getUsers()).thenReturn(List.of());

            ResponseEntity<List<UserDto>> responseEntity = userController.getUsers();
            List<UserDto> userDtos = responseEntity.getBody();

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(0, userDtos.size());
        }

        @Test
        @DisplayName("getUser method should return UserDto when user collection is not empty")
        void testGetUser() {
            User user = userMock.giveMeAnUserResponse();
            when(userService.findById("1")).thenReturn(user);

            ResponseEntity<UserDto> responseEntity = userController.getUser("1");
            UserDto userDto = responseEntity.getBody();

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(user.getId(), userDto.id());
            assertEquals(user.getName(), userDto.name());
            assertEquals(user.getEmail(), userDto.email());
            assertEquals(user.getRole(), userDto.role());
        }

        @Test
        @DisplayName("update method when update user data return user data updated")
        void testUpdateUser() throws Exception {
            Mockito.doNothing().when(userService).update(
                    Mockito.anyString(),
                    Mockito.any(User.class),
                    Mockito.anyString());

            String userId = "123";
            UserCreationDto userData = UserCreationDto.builder()
                    .name("Garfield Rei do Pedaço")
                    .email("euamolasanha@gmail.com")
                    .password("lasanhaLover")
                    .build();
            UserDetails userDetails = Mockito.mock(UserDetails.class);

            ResponseEntity<Void> response = assertDoesNotThrow(() -> userController.update(userId, userData, userDetails));
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        }

        @Test
        @DisplayName("delete method when update user data return user data updated")
        void testDeleteUser() throws Exception {
            Mockito.doNothing().when(userService).delete(
                    Mockito.anyString(),
                    Mockito.anyString());

            String userId = "123";
            UserDetails userDetails = Mockito.mock(UserDetails.class);

            ResponseEntity<Void> response = assertDoesNotThrow(() -> userController.delete(userId, userDetails));
            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        }
    }
