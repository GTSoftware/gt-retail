package ar.com.gtsoftware.api.exception;

import ar.com.gtsoftware.exception.ExceptionErrorCode;

@ExceptionErrorCode(errorCode = "404002")
public class FileNotFoundException extends RuntimeException {
    public FileNotFoundException(String message) {
        super(message);
    }

    public FileNotFoundException() {
        super("El archivo solicitado no existe");
    }
}
