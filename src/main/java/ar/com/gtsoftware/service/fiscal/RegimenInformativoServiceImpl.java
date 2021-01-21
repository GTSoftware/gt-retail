package ar.com.gtsoftware.service.fiscal;

import ar.com.gtsoftware.dto.ImportesAlicuotasIVA;
import ar.com.gtsoftware.dto.LibroIVADTO;
import ar.com.gtsoftware.dto.RegistroIVADTO;
import ar.com.gtsoftware.dto.fiscal.reginfo.*;
import ar.com.gtsoftware.enums.Parametros;
import ar.com.gtsoftware.search.LibroIVASearchFilter;
import ar.com.gtsoftware.service.ParametrosService;
import ar.com.gtsoftware.service.impl.LibroIVAComprasServiceImpl;
import ar.com.gtsoftware.service.impl.LibroIVAVentasServiceImpl;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegimenInformativoServiceImpl implements RegimenInformativoService {

  private static final String SI = "S";
  private static final String NO = "N";
  private static final String PESOS = "PES";

  private final LibroIVAVentasServiceImpl libroIVAVentasServiceImpl;
  private final LibroIVAComprasServiceImpl libroIVAComprasServiceImpl;
  private final ParametrosService parametrosService;

  @Override
  public RegimenInformativoVentas getRegimenInformativoVentas(LibroIVASearchFilter filter) {
    final LibroIVADTO libroIvaVentas = libroIVAVentasServiceImpl.obtenerLibroIVA(filter);

    RegInfoCvCabecera cabecera = buildCabecera(libroIvaVentas);

    List<RegInfoCvVentasCbte> regVentas = new LinkedList<>();
    List<RegInfoCvVentasAlicuotas> regAlicuotas = new LinkedList<>();

    for (RegistroIVADTO factura : libroIvaVentas.getFacturasList()) {
      RegInfoCvVentasCbte registro = buildRegistroComprobanteVentas(factura);

      for (ImportesAlicuotasIVA alicuotasIVA : factura.getTotalAlicuota()) {
        RegInfoCvVentasAlicuotas alicuotaComprobante =
            buildRegistroAlicuotaVentas(registro, alicuotasIVA);

        regAlicuotas.add(alicuotaComprobante);
      }

      regVentas.add(registro);
    }

    return RegimenInformativoVentas.builder()
        .cabecera(cabecera)
        .comprobantes(regVentas)
        .alicuotas(regAlicuotas)
        .build();
  }

  @Override
  public RegimenInformativoCompras getRegimenInformativoCompras(LibroIVASearchFilter filter) {
    final LibroIVADTO libroIvaCompras = libroIVAComprasServiceImpl.obtenerLibroIVA(filter);

    List<RegInfoCvComprasCbte> regVentas = new LinkedList<>();
    List<RegInfoCvComprasAlicuotas> regAlicuotas = new LinkedList<>();

    for (RegistroIVADTO factura : libroIvaCompras.getFacturasList()) {
      RegInfoCvComprasCbte registro = buildRegistroComprobanteCompras(factura);

      for (ImportesAlicuotasIVA alicuotasIVA : factura.getTotalAlicuota()) {
        RegInfoCvComprasAlicuotas alicuotaComprobante =
            buildRegistroAlicuotaCompras(registro, alicuotasIVA);

        regAlicuotas.add(alicuotaComprobante);
      }

      regVentas.add(registro);
    }

    return RegimenInformativoCompras.builder()
        .comprobantes(regVentas)
        .alicuotas(regAlicuotas)
        .build();
  }

  private RegInfoCvComprasAlicuotas buildRegistroAlicuotaCompras(
      RegInfoCvComprasCbte registro, ImportesAlicuotasIVA alicuotasIVA) {
    return RegInfoCvComprasAlicuotas.builder()
        .tipoComprobante(registro.getTipoComprobante())
        .puntoVenta(registro.getPuntoVenta())
        .numeroComprobante(registro.getNumeroComprobante())
        .codigoDocumentoVendedor(registro.getCodigoDocumentoVendedor())
        .numeroIdentificacionVendedor(registro.getNumeroIdentificacionVendedor())
        .importeNetoGravado(alicuotasIVA.getNetoGravado())
        .alicuota(alicuotasIVA.getAlicuota().getFiscalCodigoAlicuota())
        .impuestoLiquidado(alicuotasIVA.getImporteIva())
        .build();
  }

  private RegInfoCvComprasCbte buildRegistroComprobanteCompras(RegistroIVADTO factura) {
    return RegInfoCvComprasCbte.builder()
        .fechaComprobante(factura.getFechaFactura().toLocalDate())
        .tipoComprobante(factura.getCodigoTipoComprobante())
        .puntoVenta(factura.getPuntoVenta())
        .numeroComprobante(factura.getNumeroFactura())
        .despachoImportacion(null)
        .codigoDocumentoVendedor(factura.getTipoDocumentoFiscal())
        .numeroIdentificacionVendedor(factura.getDocumentoCliente())
        .denominacionVendedor(factura.getRazonSocialCliente())
        .importeTotalOperacion(factura.getTotalFactura())
        .importeTotalConceptosNoIntegranPrecioNetoGravado(BigDecimal.ZERO)
        .importeOperacionesExentas(BigDecimal.ZERO)
        .importeDePercepcionesOPagosAcuentaDeImpuestoAlValorAgregado(factura.getPercepcionIva())
        .importeDePercepcionesOPagosAcuentaDeOtrosImpuestosNacionales(BigDecimal.ZERO)
        .importeDePercepcionesDeIngresosBrutos(factura.getPercepcionIngresosBrutos())
        .importeDePercepcionesDeImpuestosMunicipales(BigDecimal.ZERO)
        .importeImpuestosInternos(BigDecimal.ZERO)
        .codigoMoneda(PESOS)
        .tipoCambio(BigDecimal.ONE)
        .cantidadAlicuotasIVA(factura.getTotalAlicuota().size())
        .codigoOperacion("3") // Productos y servicios
        .creditoFiscalComputable(factura.getTotalIva())
        .otrosTributos(BigDecimal.ZERO)
        .cuitEmisorCorredor(null)
        .ivaComision(BigDecimal.ZERO)
        .build();
  }

  private RegInfoCvVentasCbte buildRegistroComprobanteVentas(RegistroIVADTO factura) {
    return RegInfoCvVentasCbte.builder()
        .fechaComprobante(factura.getFechaFactura().toLocalDate())
        .tipoComprobante(factura.getCodigoTipoComprobante())
        .puntoVenta(factura.getPuntoVenta())
        .numeroComprobante(factura.getNumeroFactura())
        .numeroComprobanteHasta(factura.getNumeroComprobante())
        .codigoDocumentoComprador(factura.getTipoDocumentoFiscal())
        .numeroIdentificacionComprador(factura.getDocumentoCliente())
        .denominacionComprador(factura.getRazonSocialCliente())
        .importeTotalOperacion(factura.getTotalFactura())
        .importeTotalConceptosNoIntegranPrecioNetoGravado(BigDecimal.ZERO)
        .percepcionANoCategorizados(BigDecimal.ZERO)
        .importeOperacionesExentas(BigDecimal.ZERO)
        .importeDePercepcionesOPagosAcuentaDeImpuestosNacionales(BigDecimal.ZERO)
        .importeDePercepcionesDeIngresosBrutos(BigDecimal.ZERO)
        .importeDePercepcionesDeImpuestosMunicipales(BigDecimal.ZERO)
        .importeImpuestosInternos(BigDecimal.ZERO)
        .codigoMoneda(PESOS)
        .tipoCambio(BigDecimal.ONE)
        .cantidadAlicuotasIVA(factura.getTotalAlicuota().size())
        .codigoOperacion("3") // Productos y servicios
        .otrosTributos(BigDecimal.ZERO)
        .fechaVencimientoPago(factura.getFechaFactura().toLocalDate())
        .build();
  }

  private RegInfoCvVentasAlicuotas buildRegistroAlicuotaVentas(
      RegInfoCvVentasCbte registro, ImportesAlicuotasIVA alicuotasIVA) {
    return RegInfoCvVentasAlicuotas.builder()
        .tipoComprobante(registro.getTipoComprobante())
        .puntoVenta(registro.getPuntoVenta())
        .numeroComprobante(registro.getNumeroComprobante())
        .importeNetoGravado(alicuotasIVA.getNetoGravado())
        .alicuota(alicuotasIVA.getAlicuota().getFiscalCodigoAlicuota())
        .impuestoLiquidado(alicuotasIVA.getImporteIva())
        .build();
  }

  private RegInfoCvCabecera buildCabecera(LibroIVADTO libroIVADTO) {
    final String cuitInformante = parametrosService.getStringParam(Parametros.CUIT_EMPRESA);

    return RegInfoCvCabecera.builder()
        .anioPeriodo(libroIVADTO.getFechaDesde().getYear())
        .mesPeriodo(libroIVADTO.getFechaDesde().getMonthValue())
        .cuitInformante(cuitInformante)
        .secuencia(0)
        .creditoFiscalComputableGlobalOPorComprobante("2") // Por comprobante
        .importeCreditoFiscalComputableContribSegSocyOtros(BigDecimal.ZERO)
        .prorratearCreditoFiscalComputable(NO)
        .sinMovimiento(CollectionUtils.isEmpty(libroIVADTO.getFacturasList()) ? SI : NO)
        .build();
  }
}
