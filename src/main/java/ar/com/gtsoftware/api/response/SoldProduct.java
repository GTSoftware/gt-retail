package ar.com.gtsoftware.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
public class SoldProduct {

    private Long productId;
    private String productCode;
    private String supplierCode;
    private String productDescription;
    private String saleUnit;
    private BigDecimal minimumStock;
    private BigDecimal totalStock;
    private BigDecimal soldQuantity;
    private BigDecimal totalSalesCost;
    private BigDecimal salePriceTotal;
    private Integer totalSales;
    private String supplier;
}
