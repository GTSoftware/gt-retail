package ar.com.gtsoftware.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
public class DeliveryNoteDetail {
    private Long detailId;
    private Long productId;
    private String product;
    private String saleUnit;
    private BigDecimal quantity;
}
