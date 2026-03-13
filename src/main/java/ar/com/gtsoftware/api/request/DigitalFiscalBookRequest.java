package ar.com.gtsoftware.api.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class DigitalFiscalBookRequest {
  private final Long fiscalPeriodId;
  private final LocalDateTime fromDate;
  private final LocalDateTime toDate;
  @NotNull private final Kind kind;

  public enum Kind {
    SALES,
    PURCHASES,
    BOTH
  }
}
