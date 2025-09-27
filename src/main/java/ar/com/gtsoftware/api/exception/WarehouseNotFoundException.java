package ar.com.gtsoftware.api.exception;

import ar.com.gtsoftware.exception.ExceptionErrorCode;

@ExceptionErrorCode(errorCode = "404008")
public class WarehouseNotFoundException extends RuntimeException {
  public WarehouseNotFoundException(String message) {
    super(message);
  }

  public WarehouseNotFoundException() {
    super("Warehouse not found");
  }
}
