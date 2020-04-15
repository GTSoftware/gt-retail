package ar.com.gtsoftware.exception;

import lombok.*;

import java.time.LocalDateTime;

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
