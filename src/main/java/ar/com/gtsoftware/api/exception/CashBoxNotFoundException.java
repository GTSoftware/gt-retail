package ar.com.gtsoftware.api.exception;

import ar.com.gtsoftware.exception.ExceptionErrorCode;

@ExceptionErrorCode(errorCode = "404009")
public class CashBoxNotFoundException extends RuntimeException {
  public CashBoxNotFoundException(String message) {
    super(message);
  }

  public CashBoxNotFoundException() {
    super("La caja no existe.");
  }
}
