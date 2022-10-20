package ar.com.gtsoftware.api.request;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;

public record ProductPercent(@NotNull BigDecimal percentValue, @NotNull Long percentTypeId)
    implements Serializable {}
