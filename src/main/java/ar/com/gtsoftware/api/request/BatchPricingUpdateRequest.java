package ar.com.gtsoftware.api.request;

import ar.com.gtsoftware.search.ProductosSearchFilter;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public record BatchPricingUpdateRequest(
    @NotNull ProductosSearchFilter searchFilter,
    BigDecimal costUpdatePercent,
    List<ProductPercent> percentsToDelete,
    List<ProductPercent> percentsToAdd,
    String batchId) {}
