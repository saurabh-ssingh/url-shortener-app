package io.shortlink.tinyurler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // Used for global exception Handling
public class GlobalExceptionHandler {

  @ExceptionHandler(UrlNotFoundException.class)
  public ResponseEntity<Map<String,String>> handleUrlNotFoundException(UrlNotFoundException exception) {
    Map<String, String> error = new HashMap<>();
    error.put("error", exception.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
    Map<String, String> error = new HashMap<>();
    error.put("error", ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    return ResponseEntity.badRequest().body(error);
  }
}
