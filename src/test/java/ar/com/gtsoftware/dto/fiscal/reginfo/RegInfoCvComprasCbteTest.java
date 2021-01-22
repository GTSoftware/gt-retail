package ar.com.gtsoftware.dto.fiscal.reginfo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegInfoCvComprasCbteTest {
  private static final String EXPECTED =
      "202001050010000100000000000000000001                8000000000099999999999TEST, TESTER SUPER TESTER EXTR000000000012345000000000012345000000000012345000000000012345000000000012345000000000012345000000000012345000000000012345PES00010000001000000000001234500000000001234500000000000                              000000000012345";
  private RegInfoCvComprasCbte comprasCbte;

  @BeforeEach
  void setUp() {
    comprasCbte =
        RegInfoCvComprasCbte.builder()
            .fechaComprobante(LocalDate.of(2020, 1, 5))
            .tipoComprobante("001")
            .puntoVenta("0001")
            .numeroComprobante("00000001")
            .despachoImportacion(null)
            .codigoDocumentoVendedor(80)
            .numeroIdentificacionVendedor("99999999999")
            .denominacionVendedor("TEST, TESTER SUPER TESTER EXTRA LARGE TESTER")
            .importeTotalOperacion(BigDecimal.valueOf(123.45))
            .importeDePercepcionesOPagosAcuentaDeImpuestoAlValorAgregado(BigDecimal.valueOf(123.45))
            .importeTotalConceptosNoIntegranPrecioNetoGravado(BigDecimal.valueOf(123.45))
            .importeOperacionesExentas(BigDecimal.valueOf(123.45))
            .importeDePercepcionesOPagosAcuentaDeImpuestoAlValorAgregado(BigDecimal.valueOf(123.45))
            .importeDePercepcionesOPagosAcuentaDeOtrosImpuestosNacionales(
                BigDecimal.valueOf(123.45))
            .importeDePercepcionesDeIngresosBrutos(BigDecimal.valueOf(123.45))
            .importeDePercepcionesDeImpuestosMunicipales(BigDecimal.valueOf(123.45))
            .importeImpuestosInternos(BigDecimal.valueOf(123.45))
            .codigoMoneda("PES")
            .tipoCambio(BigDecimal.ONE)
            .cantidadAlicuotasIVA(1)
            .codigoOperacion("0")
            .creditoFiscalComputable(BigDecimal.valueOf(123.45))
            .otrosTributos(BigDecimal.valueOf(123.45))
            .cuitEmisorCorredor(null)
            .denominacionEmisorCorredor(null)
            .ivaComision(BigDecimal.valueOf(123.45))
            .build();
  }

  @Test
  void shouldGetStringRepresentation() {
    final String plainString = comprasCbte.toString();

    assertNotNull(plainString);
    assertThat(plainString.length(), is(325));
    assertThat(plainString, is(EXPECTED));
  }
}
