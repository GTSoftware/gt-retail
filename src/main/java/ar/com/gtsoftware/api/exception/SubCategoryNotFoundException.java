package ar.com.gtsoftware.api.exception;

import ar.com.gtsoftware.exception.ExceptionErrorCode;

@ExceptionErrorCode(errorCode = "404006")
public class SubCategoryNotFoundException extends RuntimeException {
  public SubCategoryNotFoundException(String message) {
    super(message);
  }

  public SubCategoryNotFoundException() {
    super("La sub-categoria no existe.");
  }
}
