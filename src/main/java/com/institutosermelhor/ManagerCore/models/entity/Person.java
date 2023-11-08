package com.institutosermelhor.ManagerCore.models.entity;

import com.institutosermelhor.ManagerCore.security.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Person {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;

  private String username;

  @Column(unique = true)
  private String email;

  private String password;

  private Role role;

  private boolean isDeleted = false;
}
