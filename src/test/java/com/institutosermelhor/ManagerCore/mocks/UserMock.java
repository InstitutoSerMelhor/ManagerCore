package com.institutosermelhor.ManagerCore.mocks;

import com.institutosermelhor.ManagerCore.infra.security.Role;
import com.institutosermelhor.ManagerCore.models.entity.User;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
}
