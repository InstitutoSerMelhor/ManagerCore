package com.institutosermelhor.ManagerCore.models.entity;

import com.institutosermelhor.ManagerCore.security.Role;
import java.util.Date;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Person {

  @Id
  private String id;

  private String username;

  @Indexed(unique = true)
  private String email;

  private String password;

  private Role role;

  private Date createdAt;

  private Date updatedAt;

  private boolean isDeleted = false;

  public Person(String username, String email, String password, Role role, Date createdAt,
      Date updatedAt) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.role = role;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }
}
