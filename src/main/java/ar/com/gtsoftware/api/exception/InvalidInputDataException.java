package ar.com.gtsoftware.api.exception;

import ar.com.gtsoftware.exception.ExceptionErrorCode;

@ExceptionErrorCode(errorCode = "400003")
public class InvalidInputDataException extends RuntimeException {
  public InvalidInputDataException(String message) {
    super(message);
  }

  public InvalidInputDataException() {
    super("Los datos ingresados no son válidos en este contexto.");
  }
}
