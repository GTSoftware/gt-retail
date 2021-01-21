package ar.com.gtsoftware.dto.fiscal.reginfo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class RegInfoCvCabeceraTest {

    private static final String EXPECTED = "9999999999920200200NN2000000000012345000000000012345000000000012345000000000012345000000000012345000000000012345";
    private RegInfoCvCabecera cabecera;

    @BeforeEach
    void setUp() {
        cabecera = RegInfoCvCabecera.builder()
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

    @Test
    void shouldGetStringRepresentation() {
        final String plainString = cabecera.toString();

        assertNotNull(plainString);
        assertThat(plainString.length(), is(112));
        assertThat(plainString, is(EXPECTED));
    }
}