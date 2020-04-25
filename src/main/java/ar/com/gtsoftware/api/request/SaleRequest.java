package ar.com.gtsoftware.api.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Builder
@Getter
@Setter
public class SaleRequest {
    @NotNull
    @Valid
    private List<SaleItem> products;
    @NotNull
    private Long customerId;
    @NotNull
    private Long saleConditionId;
    @NotNull
    private Long saleTypeId;
    @Valid
    private List<SalePayment> payments;
    @Size(max = 1024)
    private String observaciones;
    @Size(max = 100)
    private String remito;
    @Size(max = 100)
    private String remitente;
}
