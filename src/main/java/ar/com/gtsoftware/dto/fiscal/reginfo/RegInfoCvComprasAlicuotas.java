package ar.com.gtsoftware.dto.fiscal.reginfo;

import static ar.com.gtsoftware.dto.fiscal.reginfo.RegInfoUtils.*;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class RegInfoCvComprasAlicuotas {
  private String tipoComprobante;
  private String puntoVenta;
  private String numeroComprobante;
  private Integer codigoDocumentoVendedor;
  private String numeroIdentificacionVendedor;
  private BigDecimal importeNetoGravado;
  private Integer alicuota;
  private BigDecimal impuestoLiquidado;

  @Override
  public String toString() {
    return tipoComprobante
        + StringUtils.leftPad(puntoVenta, 5, NUMBER_PAD)
        + StringUtils.leftPad(numeroComprobante, 20, NUMBER_PAD)
        + numberPad(codigoDocumentoVendedor, 2)
        + StringUtils.leftPad(numeroIdentificacionVendedor, 20, NUMBER_PAD)
        + formatNumber(importeNetoGravado)
        + numberPad(alicuota, 4)
        + formatNumber(impuestoLiquidado);
  }
}
