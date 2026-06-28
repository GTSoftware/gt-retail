package ar.com.gtsoftware.api.response.cashbox;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CashBox(
    Long cashBoxId,
    Long branchId,
    LocalDateTime openDate,
    LocalDateTime closeDate,
    BigDecimal initialBalance,
    BigDecimal currentBalance,
    Integer version) {}
