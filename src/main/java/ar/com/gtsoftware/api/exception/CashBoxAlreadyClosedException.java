package ar.com.gtsoftware.api.exception;

import ar.com.gtsoftware.exception.ExceptionErrorCode;

@ExceptionErrorCode(errorCode = "400008")
public class CashBoxAlreadyClosedException extends RuntimeException {
  public CashBoxAlreadyClosedException(String message) {
    super(message);
  }

  public CashBoxAlreadyClosedException() {
    super("La caja ya estaba cerrada.");
  }
}
