package ar.com.gtsoftware.api.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

import ar.com.gtsoftware.api.request.DigitalFiscalBookRequest;
import ar.com.gtsoftware.dto.fiscal.reginfo.RegInfoCvCabecera;
import ar.com.gtsoftware.dto.fiscal.reginfo.RegInfoCvVentasAlicuotas;
import ar.com.gtsoftware.dto.fiscal.reginfo.RegInfoCvVentasCbte;
import ar.com.gtsoftware.dto.fiscal.reginfo.RegimenInformativoVentas;
import ar.com.gtsoftware.search.LibroIVASearchFilter;
import ar.com.gtsoftware.service.fiscal.RegimenInformativoService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import javax.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletResponse;

class FiscalBookControllerImplTest {

  private FiscalBookControllerImpl controller;

  @Mock private RegimenInformativoService regimenInformativoServiceMock;

  @Captor private ArgumentCaptor<LibroIVASearchFilter> filterArgumentCaptor;

  private HttpServletResponse responseMock;

  @BeforeEach
  void setUp() {
    initMocks(this);
    responseMock = new MockHttpServletResponse();
    controller = new FiscalBookControllerImpl(regimenInformativoServiceMock, responseMock);
  }

  @Test
  void shouldGetSalesInformativeRegime() {
    when(regimenInformativoServiceMock.getRegimenInformativoVentas(any(LibroIVASearchFilter.class)))
        .thenReturn(buildRegimenInformativoVentas());

    DigitalFiscalBookRequest request =
        DigitalFiscalBookRequest.builder()
            .fiscalPeriodId(1L)
            .kind(DigitalFiscalBookRequest.Kind.SALES)
            .build();

    controller.getInformativeRegime(request);

    verify(regimenInformativoServiceMock)
        .getRegimenInformativoVentas(filterArgumentCaptor.capture());
    verifyNoMoreInteractions(regimenInformativoServiceMock);

    final LibroIVASearchFilter filter = filterArgumentCaptor.getValue();
    assertNotNull(filter);
    assertThat(filter.getIdPeriodo(), is(1L));
    assertThat(filter.getAnuladas(), is(false));

    assertThat(responseMock.getContentType(), is("application/zip"));
    assertTrue(Long.parseLong(responseMock.getHeader(HttpHeaders.CONTENT_LENGTH)) > 0);
    assertThat(
        responseMock.getHeader(HttpHeaders.CONTENT_DISPOSITION),
        is("attachment; filename=RegimenInformativo.zip"));
  }

  private RegimenInformativoVentas buildRegimenInformativoVentas() {
    return RegimenInformativoVentas.builder()
        .cabecera(buildCabecera())
        .comprobantes(Collections.singletonList(buildRegInfoVentasComprobante()))
        .alicuotas(Collections.singletonList(buildReginfoVentasAlicuotas()))
        .build();
  }

  private RegInfoCvCabecera buildCabecera() {
    return RegInfoCvCabecera.builder()
        .sinMovimiento("N")
        .cuitInformante("99999999999")
        .mesPeriodo(2)
        .anioPeriodo(2020)
        .secuencia(0)
        .sinMovimiento("N")
        .prorratearCreditoFiscalComputable("N")
        .creditoFiscalComputableGlobalOPorComprobante("2")
        .importeCreditoFiscalComputableGlobal(BigDecimal.valueOf(123.45))
        .importeCreditoFiscalComputableConAsignacionDirecta(BigDecimal.valueOf(123.45))
        .importeCreditoFiscalNoComputableGlobal(BigDecimal.valueOf(123.45))
        .importeCreditoFiscalContribSegSocyOtros(BigDecimal.valueOf(123.45))
        .importeCreditoFiscalComputableDeterminadoPorProrrateo(BigDecimal.valueOf(123.45))
        .importeCreditoFiscalComputableContribSegSocyOtros(BigDecimal.valueOf(123.45))
        .build();
  }

  private RegInfoCvVentasAlicuotas buildReginfoVentasAlicuotas() {
    return RegInfoCvVentasAlicuotas.builder()
        .tipoComprobante("006")
        .puntoVenta("0001")
        .numeroComprobante("00000001")
        .importeNetoGravado(BigDecimal.valueOf(123.45))
        .alicuota(5)
        .impuestoLiquidado(BigDecimal.valueOf(123.45))
        .build();
  }

  private RegInfoCvVentasCbte buildRegInfoVentasComprobante() {
    return RegInfoCvVentasCbte.builder()
        .fechaComprobante(LocalDate.of(2020, 1, 5))
        .tipoComprobante("001")
        .puntoVenta("0001")
        .numeroComprobante("00000001")
        .numeroComprobanteHasta("00000001")
        .codigoDocumentoComprador(96)
        .numeroIdentificacionComprador("99999999")
        .denominacionComprador("TEST, TESTER SUPER TESTER EXTRA LARGE TESTER")
        .importeTotalOperacion(BigDecimal.valueOf(123.45))
        .importeTotalConceptosNoIntegranPrecioNetoGravado(BigDecimal.valueOf(123.45))
        .percepcionANoCategorizados(BigDecimal.valueOf(123.45))
        .importeOperacionesExentas(BigDecimal.valueOf(123.45))
        .importeDePercepcionesOPagosAcuentaDeImpuestosNacionales(BigDecimal.valueOf(123.45))
        .importeDePercepcionesDeIngresosBrutos(BigDecimal.valueOf(123.45))
        .importeDePercepcionesDeImpuestosMunicipales(BigDecimal.valueOf(123.45))
        .importeImpuestosInternos(BigDecimal.valueOf(123.45))
        .codigoMoneda("PES")
        .tipoCambio(BigDecimal.ONE)
        .cantidadAlicuotasIVA(1)
        .codigoOperacion("3")
        .otrosTributos(BigDecimal.valueOf(123.45))
        .fechaVencimientoPago(LocalDate.of(2020, 1, 5))
        .build();
  }
}
