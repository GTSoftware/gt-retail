package ar.com.gtsoftware.api.exception;

import ar.com.gtsoftware.exception.ExceptionErrorCode;

@ExceptionErrorCode(errorCode = "404005")
public class CategoryNotFoundException extends RuntimeException {
  public CategoryNotFoundException(String message) {
    super(message);
  }

  public CategoryNotFoundException() {
    super("La categoria no existe.");
  }
}
