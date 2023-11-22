package com.institutosermelhor.ManagerCore.infra.exception;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

import java.util.List;

@ControllerAdvice
public class ManagerException {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorBody> handleNotFound(NotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ErrorBody(HttpStatus.NOT_FOUND.value(), ex.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<List<DataErrorValidation>> handleValidation(MethodArgumentNotValidException ex) {
    var errors = ex.getFieldErrors();
    return ResponseEntity.badRequest()
            .body(errors.stream().map(DataErrorValidation::new).toList());
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<String> handleErr400(HttpMessageNotReadableException ex) {
    return ResponseEntity.badRequest().body(ex.getMessage());
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<String> handleErrBadCredentials() {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
  }

  @ExceptionHandler(AuthenticationException.class)
  public ResponseEntity<String> handleErrAuthentication() {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falha na autenticação");
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<String> handleErrAccessDenied() {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado");
  }

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<ErrorBody> handleConflict(ConflictException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(new ErrorBody(HttpStatus.CONFLICT.value(), ex.getMessage()));
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorBody> handleBadRequest(BadRequestException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorBody(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
  }

  @ExceptionHandler(UnauthorizedException.class)
  public ResponseEntity<ErrorBody> handleUnauthorized(UnauthorizedException ex) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(new ErrorBody(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()));
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorBody> handleRuntimeException(RuntimeException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorBody(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorBody> handleException(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorBody(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
  }

  @ExceptionHandler(Throwable.class)
  public ResponseEntity<ErrorBody> handleThrowable(Throwable ex) {
    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
        .body(new ErrorBody(HttpStatus.BAD_GATEWAY.value(), ex.getMessage()));
  }

  public record DataErrorValidation(String field, String mensage) {
    public DataErrorValidation(FieldError err) {
      this(err.getField(), err.getDefaultMessage());
    }
  }
}
