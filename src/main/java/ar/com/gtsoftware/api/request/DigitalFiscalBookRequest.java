package ar.com.gtsoftware.api.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
public class DigitalFiscalBookRequest {
    private final Long fiscalPeriodId;
    private final LocalDateTime fromDate;
    private final LocalDateTime toDate;
    private final Kind kind;

    public enum Kind {
        SALES,
        PURCHASES,
        BOTH
    }
}
