package com.institutosermelhor.ManagerCore.infra.exception;

public class ConflictException extends RuntimeException {

  public ConflictException(String message) {
    super(message);
  }

  public ConflictException() {
    super("Conflict");
  }
}
