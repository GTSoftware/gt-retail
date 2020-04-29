package ar.com.gtsoftware.api.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class StockBreak {
    private String productCode;
    private String description;
    private String saleUnit;
    private BigDecimal minimumStock;
    private BigDecimal branchStock;
    private ProductStockStatus status;
}
