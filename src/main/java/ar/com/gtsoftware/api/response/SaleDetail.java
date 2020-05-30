package ar.com.gtsoftware.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
public class SaleDetail {

    private String productCode;
    private String description;
    private String saleUnit;
    private BigDecimal quantity;
    private BigDecimal subTotal;

}
