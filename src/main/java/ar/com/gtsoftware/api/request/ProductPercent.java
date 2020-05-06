package ar.com.gtsoftware.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ProductPercent {

    @NotNull
    private final BigDecimal percentValue;
    @NotNull
    private final Long percentTypeId;
}
