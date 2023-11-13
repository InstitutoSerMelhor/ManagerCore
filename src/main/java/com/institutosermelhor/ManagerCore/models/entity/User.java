package com.institutosermelhor.ManagerCore.models.entity;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import com.institutosermelhor.ManagerCore.infra.security.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Builder
@Document
@AllArgsConstructor
public class User implements UserDetails, GrantedAuthority {

  @Id
  private String id;

  private String username;

  @Indexed(unique = true)
  private String email;

  private String password;

  private Role role;

  @Builder.Default
  private boolean isEnabled = true;

  @CreatedDate
  private Date createdAt;

  @LastModifiedDate
  private Date updatedAt;

  @Override
  public String getAuthority() {
    return this.role.getName();
  }

  @Override
  public Collection<User> getAuthorities() {
    return List.of(this);
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return this.isEnabled;
  }
}
