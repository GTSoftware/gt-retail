package ar.com.gtsoftware.api.exception;

import ar.com.gtsoftware.exception.ExceptionErrorCode;

@ExceptionErrorCode(errorCode = "404003")
public class SaleNotFoundException extends RuntimeException {
  public SaleNotFoundException(String message) {
    super(message);
  }

  public SaleNotFoundException() {
    super("El comprobante con los datos suministrados no pudo ser encontrado");
  }
}
