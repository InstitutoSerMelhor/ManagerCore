package com.institutosermelhor.ManagerCore.service;

import com.institutosermelhor.ManagerCore.infra.exception.ConflictException;
import com.institutosermelhor.ManagerCore.infra.exception.UnauthorizedException;
import com.institutosermelhor.ManagerCore.infra.security.Role;
import com.institutosermelhor.ManagerCore.models.entity.User;
import com.institutosermelhor.ManagerCore.models.repository.UserRepository;
import java.util.List;
import java.util.Objects;
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

  public User saveUser(User user) {
    UserDetails userDetails = this.loadUserByUsername(user.getEmail());
    if (userDetails != null) {
      throw new ConflictException("Email already registered!");
    }

    String hashedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
    user.setPassword(hashedPassword);
    user.setRole(Role.USER);
    return repository.save(user);
  }

  public User saveAdmin(User user) {
    UserDetails userDetails = this.loadUserByUsername(user.getEmail());
    if (userDetails != null) {
      throw new ConflictException("Email already registered!");
    }

    String hashedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
    user.setPassword(hashedPassword);
    user.setRole(Role.ADMIN);
    return repository.save(user);
  }

  public List<User> getUsers() {
    return repository.findByIsEnabledTrue();
  }

  public User findById(String userId) {
    return repository.findById(userId).orElseThrow(() -> new NotFoundException("User not found!"));
  }

  public void update(User user, String userEmail) {
    User userToUpdate = this.findById(user.getId());
    if (!Objects.equals(userToUpdate.getEmail(), userEmail)) {
      throw new UnauthorizedException();
    }
    
    userToUpdate.setName(user.getUsername());
    userToUpdate.setEmail(user.getEmail());
    userToUpdate.setPassword(user.getPassword());
    repository.save(userToUpdate);
  }

  public void delete(String userId) {
    User user = this.findById(userId);
    user.setEnabled(false);
    repository.save(user);
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    return repository.findByEmail(email);
  }
}
