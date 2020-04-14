package ar.com.gtsoftware.api.request;

import ar.com.gtsoftware.dto.domain.NegocioFormasPagoDto;
import ar.com.gtsoftware.dto.domain.NegocioPlanesPagoDetalleDto;
import ar.com.gtsoftware.dto.domain.NegocioPlanesPagoDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
public class SalePayment {
    @NotNull
    private NegocioFormasPagoDto idFormaPago;
    @NotNull
    private BigDecimal montoPago;
    private NegocioPlanesPagoDto idPlan;
    private NegocioPlanesPagoDetalleDto idDetallePlan;

}
