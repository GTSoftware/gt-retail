package ar.com.gtsoftware.api.exception;

import ar.com.gtsoftware.exception.ExceptionErrorCode;

@ExceptionErrorCode(errorCode = "404006")
public class BrandNotFoundException extends RuntimeException {
  public BrandNotFoundException(String message) {
    super(message);
  }

  public BrandNotFoundException() {
    super("La marca no existe.");
  }
}
