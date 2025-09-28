package com.epam.handler;

import com.epam.exception.BadRequestException;
import com.epam.exception.NotFoundException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentTypeMismatchException ex) {
    Map<String, Object> body = new HashMap<>();
    String paramName = ex.getName();
    String requiredType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown";
    String providedValue = ex.getValue() != null ? ex.getValue().toString() : "null";
    body.put("errorMessage", String.format("Invalid value '%s' for parameter '%s'. Please provide a valid %s value.",
        providedValue, paramName, requiredType));
    body.put("errorCode", "400");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<Map<String, Object>> handleBadRequest(BadRequestException ex) {
    Map<String, Object> body = new HashMap<>();
    body.put("errorMessage", ex.getMessage());
    body.put("errorCode", "400");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<Map<String, Object>> handleBadRequest(HttpMediaTypeNotSupportedException ex) {
    Map<String, Object> body = new HashMap<>();
    String supportedTypes = ex.getSupportedMediaTypes().toString().replace("[", "").replace("]", "");
    body.put("errorMessage", String.format("Unsupported media type. Please use one of the following supported formats: %s",
        supportedTypes.isEmpty() ? "none specified" : supportedTypes));
    body.put("errorCode", "400");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleNotFound(NotFoundException ex) {
    Map<String, Object> body = new HashMap<>();
    body.put("errorMessage", ex.getMessage());
    body.put("errorCode", "404");
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
    Map<String, Object> body = new HashMap<>();
    body.put("errorMessage", "Internal server error");
    body.put("errorCode", "500");
    System.out.println(ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
  }
}
