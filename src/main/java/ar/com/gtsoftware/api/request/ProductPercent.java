package ar.com.gtsoftware.api.request;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

public record ProductPercent(@NotNull BigDecimal percentValue, @NotNull Long percentTypeId)
    implements Serializable {}
