package ar.com.gtsoftware.service.prices;

import ar.com.gtsoftware.api.request.ProductPercent;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;

@Builder
public record UpdateProductPriceDto(
    Long productId,
    BigDecimal costUpdatePercent,
    List<ProductPercent> percentsToDelete,
    List<ProductPercent> percentsToAdd,
    String user)
    implements Serializable {}
