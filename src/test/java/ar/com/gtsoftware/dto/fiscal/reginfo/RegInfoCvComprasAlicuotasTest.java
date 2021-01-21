package ar.com.gtsoftware.dto.fiscal.reginfo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegInfoCvComprasAlicuotasTest {

  private static final String EXPECTED =
      "006000010000000000000000000180000000000999999999990000000000123450005000000000012345";

  private RegInfoCvComprasAlicuotas alicuotas;

  @BeforeEach
  void setUp() {
    alicuotas =
        RegInfoCvComprasAlicuotas.builder()
            .tipoComprobante("006")
            .puntoVenta("0001")
            .numeroComprobante("00000001")
            .codigoDocumentoVendedor(80)
            .numeroIdentificacionVendedor("99999999999")
            .importeNetoGravado(BigDecimal.valueOf(123.45))
            .alicuota(5)
            .impuestoLiquidado(BigDecimal.valueOf(123.45))
            .build();
  }

  @Test
  void shouldGetStringRepresentation() {
    final String plainString = alicuotas.toString();

    assertNotNull(plainString);
    assertThat(plainString.length(), is(84));
    assertThat(plainString, is(EXPECTED));
  }
}
