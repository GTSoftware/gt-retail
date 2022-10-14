package ar.com.gtsoftware.api.request;

import ar.com.gtsoftware.search.ProductosSearchFilter;
import java.math.BigDecimal;
import java.util.List;
import javax.validation.constraints.NotNull;

public record BatchPricingUpdateRequest(
    @NotNull ProductosSearchFilter searchFilter,
    BigDecimal costUpdatePercent,
    List<ProductPercent> percentsToDelete,
    List<ProductPercent> percentsToAdd) {}
