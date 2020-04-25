package ar.com.gtsoftware.api.exception;

import ar.com.gtsoftware.exception.ExceptionErrorCode;

@ExceptionErrorCode(errorCode = "400001")
public class CustomerValidationException extends RuntimeException {
    public CustomerValidationException(String message) {
        super(message);
    }

    public CustomerValidationException() {
        super("El cliente no pudo ser registrado con los datos ingresados.");
    }
}
