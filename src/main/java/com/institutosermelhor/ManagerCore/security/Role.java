package com.institutosermelhor.ManagerCore.security;

/**
 * Enum representing a Role.
 */
public enum Role {
  ADMIN("ADMIN"),
  USER("USER");

  private final String name;

  Role(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}