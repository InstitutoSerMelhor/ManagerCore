package com.institutosermelhor.ManagerCore.mocks;

import com.institutosermelhor.ManagerCore.infra.security.Role;
import com.institutosermelhor.ManagerCore.models.entity.User;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class UserMock {
    private String password = "odeS@iDoMeuSofa";
    public User giveMeAnUser() {
        String passwordHashed = new BCryptPasswordEncoder().encode(password);
        return new User(
                null,
                "Garfield",
                "lasanha@gmail.com",
                passwordHashed,
                Role.ADMIN,
                true,
                null,
                null);
    }

    public User giveMeAnUserResponse() {
        String passwordHashed = new BCryptPasswordEncoder().encode(password);
        return new User(
                "1",
                "Garfield",
                "lasanha@gmail.com",
                passwordHashed,
                Role.ADMIN,
                true,
                new Date(),
                new Date());
    }
}
