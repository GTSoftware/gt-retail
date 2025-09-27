package ar.com.gtsoftware.api.request;

import ar.com.gtsoftware.search.ProductosSearchFilter;
import java.math.BigDecimal;
import java.util.List;
import jakarta.validation.constraints.NotNull;

public record BatchPricingUpdateRequest(
    @NotNull ProductosSearchFilter searchFilter,
    BigDecimal costUpdatePercent,
    List<ProductPercent> percentsToDelete,
    List<ProductPercent> percentsToAdd,
    String batchId) {}
