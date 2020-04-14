package ar.com.gtsoftware.exception;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@RestController
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex, WebRequest request) {

        final String errorCode = getErrorCode(ex);
        final ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
                .details(request.getDescription(false))
                .errorCode(errorCode)
                .build();

        final int statusCode = Integer.parseInt(StringUtils.left(errorCode, 3));
        return new ResponseEntity(exceptionResponse, HttpStatus.valueOf(statusCode));
    }

    private String getErrorCode(Exception ex) {
        String errorCode = "500999";
        Class<?> clazz = ex.getClass();
        if (clazz.isAnnotationPresent(ExceptionErrorCode.class)) {
            errorCode = clazz.getAnnotation(ExceptionErrorCode.class).errorCode();
        }

        return errorCode;
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        final ValidationExceptionResponse validationExceptionResponse = new ValidationExceptionResponse();
        validationExceptionResponse.setMessage("Validation error");
        validationExceptionResponse.setFieldErrors(getFieldErrors(ex));
        validationExceptionResponse.setTimestamp(LocalDateTime.now());
        validationExceptionResponse.setErrorCode("400999");

        return new ResponseEntity(validationExceptionResponse, status);
    }

    private List<String> getFieldErrors(MethodArgumentNotValidException ex) {
        final List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        final List<String> fieldsWithErrors = new ArrayList<>(fieldErrors.size());

        for (FieldError error : fieldErrors) {
            fieldsWithErrors.add(error.getField() + ": " + error.getDefaultMessage());
        }

        return fieldsWithErrors;
    }

}
