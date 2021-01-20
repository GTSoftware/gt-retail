package ar.com.gtsoftware.dto.fiscal.reginfo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RegInfoCvVentasAlicuotasTest {

    private static final String EXPECTED = "00600001000000000000000000010000000000123450005000000000012345";

    private RegInfoCvVentasAlicuotas alicuotas;

    @BeforeEach
    void setUp() {
        alicuotas = RegInfoCvVentasAlicuotas.builder()
                .tipoComprobante("006")
                .puntoVenta("0001")
                .numeroComprobante("00000001")
                .importeNetoGravado(BigDecimal.valueOf(123.45))
                .alicuota(5)
                .impuestoLiquidado(BigDecimal.valueOf(123.45))
                .build();
    }

    @Test
    void shouldGetStringRepresentation() {
        final String plainString = alicuotas.toString();

        assertNotNull(plainString);
        assertThat(plainString.length(), is(62));
        assertThat(plainString, is(EXPECTED));
    }
}