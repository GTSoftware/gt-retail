package ar.com.gtsoftware.api.exception;

import ar.com.gtsoftware.exception.ExceptionErrorCode;

@ExceptionErrorCode(errorCode = "400004")
public class DuplicatedProductCodeException extends RuntimeException {
  public DuplicatedProductCodeException(String message) {
    super(message);
  }
}
