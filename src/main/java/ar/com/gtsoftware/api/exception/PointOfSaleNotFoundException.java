package ar.com.gtsoftware.api.exception;

import ar.com.gtsoftware.exception.ExceptionErrorCode;

@ExceptionErrorCode(errorCode = "404004")
public class PointOfSaleNotFoundException extends RuntimeException {
    public PointOfSaleNotFoundException(String message) {
        super(message);
    }

    public PointOfSaleNotFoundException() {
        super("El punto de venta no existe");
    }
}
