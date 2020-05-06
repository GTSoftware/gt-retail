package ar.com.gtsoftware.api.exception;

import ar.com.gtsoftware.exception.ExceptionErrorCode;

@ExceptionErrorCode(errorCode = "401002")
public class UserNotAllowedException extends RuntimeException {

    public UserNotAllowedException() {
        super("You don't have access to this resource");
    }

}
