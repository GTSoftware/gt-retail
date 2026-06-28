package ar.com.gtsoftware.api.response.cashbox;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CashMovementSearchResult(
    Long movementId,
    Long boxId,
    LocalDateTime movementDate,
    BigDecimal amount,
    String description) {}
