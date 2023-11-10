package com.institutosermelhor.ManagerCore.models.entity;

import java.util.Date;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import com.institutosermelhor.ManagerCore.infra.security.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Document
@AllArgsConstructor
public class Person {

  @Id
  private String id;

  private String username;

  @Indexed(unique = true)
  private String email;

  private String password;

  private Role role;

  @CreatedDate
  private Date createdAt;

  @LastModifiedDate
  private Date updatedAt;

  @Builder.Default
  private boolean isDeleted = false;

}
