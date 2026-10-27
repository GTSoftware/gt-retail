package ar.com.gtsoftware.api.response.cashbox;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CashBoxArqueo(
    Long id,
    Long userId,
    String userName,
    Long controlUserId,
    String controlUserName,
    Long branchId,
    Long cashBoxId,
    LocalDateTime arqueoDate,
    LocalDateTime controlDate,
    BigDecimal initialBalance,
    BigDecimal finalBalance,
    String controlNotes,
    List<CashBoxArqueoDetail> details) {}
