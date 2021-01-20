package ar.com.gtsoftware.dto.fiscal.reginfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;

import static ar.com.gtsoftware.dto.fiscal.reginfo.RegInfoUtils.*;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class ReginfoCvVentasCbte {

    private LocalDate fechaComprobante;

    private String tipoComprobante;

    private String puntoVenta;

    private String numeroComprobante;

    private String numeroComprobanteHasta;


    private Integer codigoDocumentoComprador;

    private String numeroIdentificacionComprador;

    private String denominacionComprador;

    private BigDecimal importeTotalOperacion;

    private BigDecimal importeTotalConceptosNoIntegranPrecioNetoGravado;

    private BigDecimal percepcionANoCategorizados;

    private BigDecimal importeOperacionesExentas;

    private BigDecimal importeDePercepcionesOPagosAcuentaDeImpuestosNacionales;

    private BigDecimal importeDePercepcionesDeIngresosBrutos;

    private BigDecimal importeDePercepcionesDeImpuestosMunicipales;

    private BigDecimal importeImpuestosInternos;

    private String codigoMoneda;
    //4 enteros y 6 decimales sin punto
    private BigDecimal tipoCambio;

    private Integer cantidadAlicuotasIVA;

    private String codigoOperacion;

    private BigDecimal otrosTributos;

    private LocalDate fechaVencimientoPago;

    @Override
    public String toString() {
        return fechaComprobante.format(DATE_TIME_FORMATTER) +
                tipoComprobante +
                StringUtils.leftPad(puntoVenta, 5, NUMBER_PAD) +
                StringUtils.leftPad(numeroComprobante, 20, NUMBER_PAD) +
                StringUtils.leftPad(numeroComprobanteHasta, 20, NUMBER_PAD) +
                numberPad(codigoDocumentoComprador, 2) +
                StringUtils.leftPad(numeroIdentificacionComprador, 20, NUMBER_PAD) +
                StringUtils.rightPad(StringUtils.left(denominacionComprador, 30), 30, StringUtils.SPACE) +
                formatNumber(importeTotalOperacion) +
                formatNumber(importeTotalConceptosNoIntegranPrecioNetoGravado) +
                formatNumber(percepcionANoCategorizados) +
                formatNumber(importeOperacionesExentas) +
                formatNumber(importeDePercepcionesOPagosAcuentaDeImpuestosNacionales) +
                formatNumber(importeDePercepcionesDeIngresosBrutos) +
                formatNumber(importeDePercepcionesDeImpuestosMunicipales) +
                formatNumber(importeImpuestosInternos) +
                StringUtils.leftPad(codigoMoneda, 3, NUMBER_PAD) +
                formatTipoCambio(tipoCambio) +
                cantidadAlicuotasIVA +
                codigoOperacion +
                formatNumber(otrosTributos) +
                fechaVencimientoPago.format(DATE_TIME_FORMATTER);
    }
}
