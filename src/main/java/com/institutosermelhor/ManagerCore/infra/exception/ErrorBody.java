package com.institutosermelhor.ManagerCore.infra.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorBody {

  private int status;
  private String message;

}
