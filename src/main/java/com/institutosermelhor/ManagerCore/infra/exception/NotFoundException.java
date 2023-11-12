package com.institutosermelhor.ManagerCore.infra.exception;


public class NotFoundException extends RuntimeException {

  public NotFoundException(String message) {
    super(message);
  }

  public NotFoundException() {
    super("Not found!");
  }

}
