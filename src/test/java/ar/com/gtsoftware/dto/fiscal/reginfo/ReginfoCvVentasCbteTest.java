package ar.com.gtsoftware.dto.fiscal.reginfo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ReginfoCvVentasCbteTest {

    private static final String EXPECTED = "202001050010000100000000000000000001000000000000000000019600000000000099999999TEST, TESTER SUPER TESTER EXTR000000000012345000000000012345000000000012345000000000012345000000000012345000000000012345000000000012345000000000012345PES00010000001300000000001234520200105";
    private ReginfoCvVentasCbte ventasCbte;

    @BeforeEach
    void setUp() {
        ventasCbte = ReginfoCvVentasCbte.builder()
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

    @Test
    void shouldGetStringRepresentation() {
        final String plainString = ventasCbte.toString();

        assertNotNull(plainString);
        assertThat(plainString.length(), is(266));
        assertThat(plainString, is(EXPECTED));
    }
}