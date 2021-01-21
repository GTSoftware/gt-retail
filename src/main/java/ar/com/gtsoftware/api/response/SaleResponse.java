package ar.com.gtsoftware.api.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SaleResponse {
  private Long saleId;
  private String customer;
  private LocalDateTime saleDate;
  private String invoiceNumber;
  private String user;
  private String branch;
  private String saleCondition;
  private String saleType;
  private String observations;
  private String remito;
  private String remitente;
  private String invoiceChar;
  private BigDecimal total;
  private BigDecimal remainingAmount;
  private List<SaleDetail> details;
}
