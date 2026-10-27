package ar.com.gtsoftware.api.response.cashbox;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CashBoxArqueoSearchResult(
    Long id,
    Long userId,
    String userName,
    Long branchId,
    Long cashBoxId,
    LocalDateTime arqueoDate,
    BigDecimal initialBalance,
    BigDecimal finalBalance,
    boolean controlled) {}
