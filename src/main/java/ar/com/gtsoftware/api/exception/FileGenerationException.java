package ar.com.gtsoftware.api.exception;

import ar.com.gtsoftware.exception.ExceptionErrorCode;

@ExceptionErrorCode(errorCode = "500001")
public class FileGenerationException extends RuntimeException {
    public FileGenerationException(String message) {
        super(message);
    }

    public FileGenerationException() {
        super("Ocurrio un problema al generar el archivo");
    }
}
