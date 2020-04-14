package ar.com.gtsoftware.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class AddCartItemRequest {
    private Long productId;
    private String productCode;
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal cantidad;
}
