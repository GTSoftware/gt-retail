package ar.com.gtsoftware.api.request;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDateTime;

@Getter
public class ProductMovementRequest {

    @NotNull
    @Past
    private LocalDateTime fromDate;
    @NotNull
    private Long productId;
    @NotNull
    private Long warehouseId;

}
