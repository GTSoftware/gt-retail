package ar.com.gtsoftware.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FiscalBookRequest {
    private final Long fiscalPeriodId;
    private final LocalDateTime fromDate;
    private final LocalDateTime toDate;
}
