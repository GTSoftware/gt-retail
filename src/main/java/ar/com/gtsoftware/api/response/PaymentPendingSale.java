package ar.com.gtsoftware.api.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
public class PaymentPendingSale {
  private Long saleId;
  private BigDecimal total;
  private BigDecimal remainingAmount;
  private LocalDateTime saleDate;
  private String saleType;
  private String user;
  private String branch;
  private String customer;
  private Long customerId;
  private String saleCondition;
  private String invoiceNumber;
}
