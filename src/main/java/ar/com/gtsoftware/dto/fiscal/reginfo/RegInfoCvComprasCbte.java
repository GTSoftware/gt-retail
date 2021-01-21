package ar.com.gtsoftware.dto.fiscal.reginfo;

import static ar.com.gtsoftware.dto.fiscal.reginfo.RegInfoUtils.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class RegInfoCvComprasCbte {
  private LocalDate fechaComprobante;
  private String tipoComprobante;
  private String puntoVenta;
  private String numeroComprobante;
  private String despachoImportacion;
  private Integer codigoDocumentoVendedor;
  private String numeroIdentificacionVendedor;
  private String denominacionVendedor;
  private BigDecimal importeTotalOperacion;
  private BigDecimal importeTotalConceptosNoIntegranPrecioNetoGravado;
  private BigDecimal importeOperacionesExentas;
  private BigDecimal importeDePercepcionesOPagosAcuentaDeImpuestoAlValorAgregado;
  private BigDecimal importeDePercepcionesOPagosAcuentaDeOtrosImpuestosNacionales;
  private BigDecimal importeDePercepcionesDeIngresosBrutos;
  private BigDecimal importeDePercepcionesDeImpuestosMunicipales;
  private BigDecimal importeImpuestosInternos;
  private String codigoMoneda;
  private BigDecimal tipoCambio;
  private Integer cantidadAlicuotasIVA;
  private String codigoOperacion;
  private BigDecimal creditoFiscalComputable;
  private BigDecimal otrosTributos;
  private String cuitEmisorCorredor;
  private String denominacionEmisorCorredor;
  private BigDecimal ivaComision;

  @Override
  public String toString() {
    return fechaComprobante.format(DATE_TIME_FORMATTER)
        + tipoComprobante
        + StringUtils.leftPad(puntoVenta, 5, NUMBER_PAD)
        + StringUtils.leftPad(numeroComprobante, 20, NUMBER_PAD)
        + StringUtils.rightPad(StringUtils.trimToEmpty(despachoImportacion), 16, StringUtils.EMPTY)
        + numberPad(codigoDocumentoVendedor, 2)
        + StringUtils.leftPad(numeroIdentificacionVendedor, 20, NUMBER_PAD)
        + StringUtils.rightPad(StringUtils.left(denominacionVendedor, 30), 30, StringUtils.SPACE)
        + formatNumber(importeTotalOperacion)
        + formatNumber(importeTotalConceptosNoIntegranPrecioNetoGravado)
        + formatNumber(importeOperacionesExentas)
        + formatNumber(importeDePercepcionesOPagosAcuentaDeImpuestoAlValorAgregado)
        + formatNumber(importeDePercepcionesOPagosAcuentaDeOtrosImpuestosNacionales)
        + formatNumber(importeDePercepcionesDeIngresosBrutos)
        + formatNumber(importeDePercepcionesDeImpuestosMunicipales)
        + formatNumber(importeImpuestosInternos)
        + StringUtils.leftPad(codigoMoneda, 3, NUMBER_PAD)
        + formatTipoCambio(tipoCambio)
        + cantidadAlicuotasIVA
        + codigoOperacion
        + formatNumber(creditoFiscalComputable)
        + formatNumber(otrosTributos)
        + StringUtils.leftPad(StringUtils.trimToEmpty(cuitEmisorCorredor), 11, NUMBER_PAD)
        + StringUtils.rightPad(
            StringUtils.trimToEmpty(denominacionEmisorCorredor), 30, StringUtils.EMPTY)
        + formatNumber(ivaComision);
  }
}
