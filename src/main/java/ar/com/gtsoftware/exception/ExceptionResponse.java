package ar.com.gtsoftware.exception;

import java.time.LocalDateTime;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExceptionResponse {
  private LocalDateTime timestamp;
  private String message;
  private String details;
  private String errorCode;
}
