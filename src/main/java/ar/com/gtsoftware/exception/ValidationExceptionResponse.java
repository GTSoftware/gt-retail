package ar.com.gtsoftware.exception;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ValidationExceptionResponse extends ExceptionResponse {

  private List<String> fieldErrors;
}
