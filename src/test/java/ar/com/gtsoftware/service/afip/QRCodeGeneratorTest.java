package ar.com.gtsoftware.service.afip;

import ar.com.gtsoftware.domain.FiscalLibroIvaVentas;
import ar.com.gtsoftware.domain.FiscalTiposComprobante;
import ar.com.gtsoftware.domain.LegalTiposDocumento;
import ar.com.gtsoftware.domain.Personas;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QRCodeGeneratorTest {

  private QRCodeGenerator generador;

  private static final String EXPECTED_RESULT_URL =
      "https://www.afip.gob.ar/fe/qr/?p=eyJ2ZXIiOjEsImZlY2hhIjoiMjAyMC0xMC0xMyIsImN1aXQiOjMwMDAwMDAwMDA3LCJwdG9WdGEiOjEwLCJ0aXBvQ21wIjoxLCJucm9DbXAiOjk0LCJpbXBvcnRlIjoxMjEwMCwibW9uZWRhIjoiUEVTIiwiY3R6IjoxLCJ0aXBvRG9jUmVjIjo4MCwibnJvRG9jUmVjIjoyMDAwMDAwMDAwMSwidGlwb0NvZEF1dCI6IkUiLCJjb2RBdXQiOjcwNDE3MDU0MzY3NDc2fQ==";

  private static final Long CUIT_EMPRESA = 30000000007L;

  @BeforeEach
  void setUp() {
    generador = new QRCodeGenerator();
  }

  @Test
  void shouldGenerateQRString() {
    FiscalLibroIvaVentas registro = getExampleFiscalInvoice();

    final String result = generador.generarCodigo(registro, CUIT_EMPRESA);

    Assertions.assertEquals(EXPECTED_RESULT_URL, result);
  }

  @Test
  void shouldGenerateEmptyQRStringOnNoCAE() {
    FiscalLibroIvaVentas registro = getExampleFiscalInvoice();
    registro.setCae(null);

    final String result = generador.generarCodigo(registro, CUIT_EMPRESA);

    Assertions.assertEquals(StringUtils.EMPTY, result);
  }

  private FiscalLibroIvaVentas getExampleFiscalInvoice() {
    FiscalLibroIvaVentas registro = new FiscalLibroIvaVentas();
    registro.setPuntoVentaFactura("0010");
    registro.setCae(70417054367476L);
    registro.setFechaFactura(LocalDateTime.of(2020, 10, 13, 14, 10));

    FiscalTiposComprobante tipoComp = new FiscalTiposComprobante();
    tipoComp.setCodigoTipoComprobante("1");
    registro.setCodigoTipoComprobante(tipoComp);
    registro.setNumeroFactura("94");
    registro.setTotalFactura(BigDecimal.valueOf(12100));

    Personas cliente = new Personas();
    LegalTiposDocumento tipoDocumentoCliente = new LegalTiposDocumento();
    tipoDocumentoCliente.setFiscalCodigoTipoDocumento(80);
    cliente.setIdTipoDocumento(tipoDocumentoCliente);
    registro.setIdPersona(cliente);
    registro.setDocumento("20000000001");

    return registro;
  }
}
