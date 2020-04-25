package ar.com.gtsoftware.api.response;

import ar.com.gtsoftware.dto.domain.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class PaymentPendingSale {
    private Long id;
    private BigDecimal total;
    private BigDecimal saldo;
    private String observaciones;
    private NegocioTiposComprobanteDto tipoComprobante;
    private UsuariosDto usuario;
    private SucursalesDto sucursal;
    private PersonasDto cliente;
    private NegocioCondicionesOperacionesDto condicionComprobante;
    private String factura;
}
