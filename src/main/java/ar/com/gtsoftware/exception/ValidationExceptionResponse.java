package ar.com.gtsoftware.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ValidationExceptionResponse extends ExceptionResponse {

    private List<String> fieldErrors;
}
