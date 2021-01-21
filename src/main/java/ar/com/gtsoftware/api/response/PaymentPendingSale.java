package ar.com.gtsoftware.api.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
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
