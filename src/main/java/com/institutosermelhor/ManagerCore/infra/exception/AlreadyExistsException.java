package com.institutosermelhor.ManagerCore.infra.exception;

public class AlreadyExistsException extends RuntimeException {

  public AlreadyExistsException(String message) {
    super(message);
  }

  public AlreadyExistsException() {
    super("Already exists!");
  }
}
