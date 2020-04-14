package ar.com.gtsoftware.api.request;

import ar.com.gtsoftware.dto.domain.NegocioCondicionesOperacionesDto;
import ar.com.gtsoftware.dto.domain.NegocioTiposComprobanteDto;
import ar.com.gtsoftware.dto.domain.PersonasDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Builder
@Getter
@Setter
public class SaleRequest {
    @NotNull
    private List<SaleItem> products;
    @NotNull
    private PersonasDto customerInfo;
    @NotNull
    private NegocioCondicionesOperacionesDto saleCondition;
    @NotNull
    private NegocioTiposComprobanteDto saleType;
    private List<SalePayment> payments;
    @Size(max = 1024)
    private String observaciones;
    @Size(max = 100)
    private String remito;
    @Size(max = 100)
    private String remitente;
}
