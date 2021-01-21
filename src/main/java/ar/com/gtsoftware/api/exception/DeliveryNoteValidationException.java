package ar.com.gtsoftware.api.exception;

import ar.com.gtsoftware.exception.ExceptionErrorCode;

@ExceptionErrorCode(errorCode = "400002")
public class DeliveryNoteValidationException extends RuntimeException {
  public DeliveryNoteValidationException(String message) {
    super(message);
  }
}
