package ar.com.gtsoftware.api.exception;

import ar.com.gtsoftware.exception.ExceptionErrorCode;

@ExceptionErrorCode(errorCode = "400005")
public class IdempotenceException extends RuntimeException {
  public IdempotenceException(String message) {
    super(message);
  }
}
