package ar.com.gtsoftware.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class DeliveryNoteItem {
    @NotNull
    private final Long productId;
    @NotNull
    @DecimalMin("0.01")
    private final BigDecimal quantity;
    private final int itemNumber;
}
