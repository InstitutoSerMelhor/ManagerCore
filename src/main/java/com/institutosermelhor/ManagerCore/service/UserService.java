package com.institutosermelhor.ManagerCore.service;

import com.institutosermelhor.ManagerCore.infra.exception.AlreadyExistsException;
import com.institutosermelhor.ManagerCore.infra.security.Role;
import com.institutosermelhor.ManagerCore.models.entity.User;
import com.institutosermelhor.ManagerCore.models.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.institutosermelhor.ManagerCore.infra.exception.NotFoundException;

@Service
public class UserService implements UserDetailsService {

  UserRepository repository;

  @Autowired
  public UserService(UserRepository repository) {
    this.repository = repository;
  }

  public User create(User user) {
    UserDetails userDetails = this.loadUserByUsername(user.getEmail());
    if (userDetails != null) {
      throw new AlreadyExistsException("Email already registered!");
    }

    String hashedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
    user.setPassword(hashedPassword);
    user.setRole(Role.USER);
    return repository.save(user);
  }

  public User createAdmin(User user) {
    UserDetails userDetails = this.loadUserByUsername(user.getEmail());
    if (userDetails != null) {
      throw new AlreadyExistsException("Email already registered!");
    }

    String hashedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
    user.setPassword(hashedPassword);
    user.setRole(Role.ADMIN);
    return repository.save(user);
  }

  public List<User> getUsers() {
    return repository.findAll();
  }

  public User findById(String userId) {
    return repository.findById(userId).orElseThrow(() -> new NotFoundException("User not found!"));
  }

  public void delete(String userId) {
    User user = findById(userId);
    user.setEnabled(false);
    repository.save(user);
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return repository.findByEmail(email);
  }
}
