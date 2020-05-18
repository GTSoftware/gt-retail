package ar.com.gtsoftware.api.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class SaleSearchResult {
    private Long saleId;
    private String customer;
    private LocalDateTime saleDate;
    private String invoiceNumber;
    private String user;
    private String branch;
    private String saleCondition;
    private String saleType;
    private BigDecimal total;
    private BigDecimal remainingAmount;

}
