package ar.com.gtsoftware.service.afip;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.ws.test.client.RequestMatchers.payload;
import static org.springframework.ws.test.client.ResponseCreators.withPayload;

import ar.com.gtsoftware.dto.fiscal.CAEResponse;
import ar.com.gtsoftware.entity.*;
import ar.com.gtsoftware.enums.Parametros;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import javax.xml.transform.Source;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ws.test.client.MockWebServiceServer;
import org.springframework.xml.transform.StringSource;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AfipClientTestConfiguration.class)
class ElectronicInvoiceClientTest {

  @Autowired private ElectronicInvoiceClient client;

  private MockWebServiceServer mockServer;

  @BeforeEach
  void setUp() {
    when(client.getParametrosService().getLongParam(Parametros.EMPRESA_CUIT))
        .thenReturn(99999999999L);

    mockServer = MockWebServiceServer.createServer(client);
  }

  @Test
  void shouldGetLastAuthorizedInvoiceNumber() {
    final AFIPAuthServices afipAuthServices = new AFIPAuthServices();
    afipAuthServices.setSign("sign");
    afipAuthServices.setToken("token");

    Source requestPayload =
        new StringSource(
            "<ns2:FECompUltimoAutorizado xmlns:ns2=\"http://ar.gov.afip.dif.FEV1/\">\n"
                + "    <ns2:Auth>\n"
                + "        <ns2:Token>token</ns2:Token>\n"
                + "        <ns2:Sign>sign</ns2:Sign>\n"
                + "        <ns2:Cuit>99999999999</ns2:Cuit>\n"
                + "    </ns2:Auth>\n"
                + "    <ns2:PtoVta>1</ns2:PtoVta>\n"
                + "    <ns2:CbteTipo>1</ns2:CbteTipo>\n"
                + "</ns2:FECompUltimoAutorizado>");

    Source responsePayload =
        new StringSource(
            "<FECompUltimoAutorizadoResponse xmlns=\"http://ar.gov.afip.dif.FEV1/\">\n"
                + "    <FECompUltimoAutorizadoResult>\n"
                + "        <PtoVta>1</PtoVta>\n"
                + "        <CbteTipo>1</CbteTipo>\n"
                + "        <CbteNro>14</CbteNro>\n"
                + "        <Errors>\n"
                + "            <Err>\n"
                + "                <Code>int</Code>\n"
                + "                <Msg>string</Msg>\n"
                + "            </Err>\n"
                + "            <Err>\n"
                + "                <Code>int</Code>\n"
                + "                <Msg>string</Msg>\n"
                + "            </Err>\n"
                + "        </Errors>\n"
                + "        <Events>\n"
                + "            <Evt>\n"
                + "                <Code>int</Code>\n"
                + "                <Msg>string</Msg>\n"
                + "            </Evt>\n"
                + "            <Evt>\n"
                + "                <Code>int</Code>\n"
                + "                <Msg>string</Msg>\n"
                + "            </Evt>\n"
                + "        </Events>\n"
                + "    </FECompUltimoAutorizadoResult>\n"
                + "</FECompUltimoAutorizadoResponse>");

    mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));

    final int lastAuthorizedInvoiceNumber =
        client.getLastAuthorizedInvoiceNumber(afipAuthServices, 1, 1);

    assertThat(lastAuthorizedInvoiceNumber, is(14));
    mockServer.verify();
  }

  @Test
  void shouldRequestElectronicAuthorization() {
    final AFIPAuthServices afipAuthServices = new AFIPAuthServices();
    afipAuthServices.setSign("sign");
    afipAuthServices.setToken("token");

    Source requestPayload =
        new StringSource(
            "<ns2:FECAESolicitar xmlns:ns2=\"http://ar.gov.afip.dif.FEV1/\">\n"
                + "    <ns2:Auth>\n"
                + "        <ns2:Token>token</ns2:Token>\n"
                + "        <ns2:Sign>sign</ns2:Sign>\n"
                + "        <ns2:Cuit>99999999999</ns2:Cuit>\n"
                + "    </ns2:Auth>\n"
                + "    <ns2:FeCAEReq>\n"
                + "        <ns2:FeCabReq>\n"
                + "            <ns2:CantReg>1</ns2:CantReg>\n"
                + "            <ns2:PtoVta>1</ns2:PtoVta>\n"
                + "            <ns2:CbteTipo>99</ns2:CbteTipo>\n"
                + "        </ns2:FeCabReq>\n"
                + "        <ns2:FeDetReq>\n"
                + "            <ns2:FECAEDetRequest>\n"
                + "                <ns2:Concepto>3</ns2:Concepto>\n"
                + "                <ns2:DocTipo>99</ns2:DocTipo>\n"
                + "                <ns2:DocNro>88888888</ns2:DocNro>\n"
                + "                <ns2:CbteDesde>1</ns2:CbteDesde>\n"
                + "                <ns2:CbteHasta>1</ns2:CbteHasta>\n"
                + "                <ns2:CbteFch>20200101</ns2:CbteFch>\n"
                + "                <ns2:ImpTotal>10.53</ns2:ImpTotal>\n"
                + "                <ns2:ImpTotConc>0.0</ns2:ImpTotConc>\n"
                + "                <ns2:ImpNeto>10.0</ns2:ImpNeto>\n"
                + "                <ns2:ImpOpEx>10.0</ns2:ImpOpEx>\n"
                + "                <ns2:ImpIVA>10.0</ns2:ImpIVA>\n"
                + "                <ns2:ImpTrib>10.0</ns2:ImpTrib>\n"
                + "                <ns2:FchServDesde>20200101</ns2:FchServDesde>\n"
                + "                <ns2:FchServHasta>20200101</ns2:FchServHasta>\n"
                + "                <ns2:FchVtoPago>20200101</ns2:FchVtoPago>\n"
                + "                <ns2:MonId>PES</ns2:MonId>\n"
                + "                <ns2:MonCotiz>1.0</ns2:MonCotiz>\n"
                + "                <ns2:Iva>\n"
                + "                    <ns2:AlicIva>\n"
                + "                        <ns2:Id>1</ns2:Id>\n"
                + "                        <ns2:BaseImp>10.0</ns2:BaseImp>\n"
                + "                        <ns2:Importe>10.0</ns2:Importe>\n"
                + "                    </ns2:AlicIva>\n"
                + "                </ns2:Iva>\n"
                + "            </ns2:FECAEDetRequest>\n"
                + "        </ns2:FeDetReq>\n"
                + "    </ns2:FeCAEReq>\n"
                + "</ns2:FECAESolicitar>");

    Source responsePayload =
        new StringSource(
            "<FECAESolicitarResponse xmlns=\"http://ar.gov.afip.dif.FEV1/\">\n"
                + "      <FECAESolicitarResult>\n"
                + "        <FeCabResp><Resultado>A</Resultado></FeCabResp>\n"
                + "        <FeDetResp>\n"
                + "          <FECAEDetResponse>\n"
                + "            <CAE>1234</CAE>\n"
                + "            <CAEFchVto>20200106</CAEFchVto>\n"
                + "          </FECAEDetResponse>\n"
                + "        </FeDetResp>\n"
                + "        <Events>\n"
                + "          <Evt>\n"
                + "            <Code>int</Code>\n"
                + "            <Msg>string</Msg>\n"
                + "          </Evt>\n"
                + "          <Evt>\n"
                + "            <Code>int</Code>\n"
                + "            <Msg>string</Msg>\n"
                + "          </Evt>\n"
                + "        </Events>\n"
                + "        <Errors>\n"
                + "          <Err>\n"
                + "            <Code>int</Code>\n"
                + "            <Msg>string</Msg>\n"
                + "          </Err>\n"
                + "          <Err>\n"
                + "            <Code>int</Code>\n"
                + "            <Msg>string</Msg>\n"
                + "          </Err>\n"
                + "        </Errors>\n"
                + "      </FECAESolicitarResult>\n"
                + "    </FECAESolicitarResponse>");

    mockServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));

    final CAEResponse caeResponse =
        client.requestElectronicAuthorization(afipAuthServices, buildDummyIvaVentas());

    assertThat(caeResponse.getCae(), is(1234L));
    assertThat(caeResponse.getFechaVencimientoCae(), is(LocalDate.of(2020, 1, 6)));
    mockServer.verify();
  }

  private FiscalLibroIvaVentas buildDummyIvaVentas() {
    final FiscalLibroIvaVentas dummyLibro = new FiscalLibroIvaVentas();
    dummyLibro.setDocumento("88888888");
    dummyLibro.setNumeroFactura("1");

    final FiscalTiposComprobante fiscalTiposComprobante = new FiscalTiposComprobante();
    fiscalTiposComprobante.setCodigoTipoComprobante("99");
    dummyLibro.setCodigoTipoComprobante(fiscalTiposComprobante);

    dummyLibro.setFechaFactura(LocalDateTime.of(2020, 1, 1, 13, 21));
    dummyLibro.setImporteExento(BigDecimal.TEN);
    dummyLibro.setImporteIva(BigDecimal.TEN);
    dummyLibro.setImporteNetoGravado(BigDecimal.TEN);
    dummyLibro.setImporteTributos(BigDecimal.TEN);
    dummyLibro.setImporteNetoNoGravado(BigDecimal.TEN);
    dummyLibro.setTotalFactura(new BigDecimal("10.53"));
    dummyLibro.setPuntoVentaFactura("1");

    Personas persona = new Personas();
    final LegalTiposDocumento tipoDoc = new LegalTiposDocumento();
    tipoDoc.setFiscalCodigoTipoDocumento(99);
    persona.setIdTipoDocumento(tipoDoc);
    dummyLibro.setIdPersona(persona);

    FiscalLibroIvaVentasLineas linea = new FiscalLibroIvaVentasLineas();
    linea.setNoGravado(BigDecimal.TEN);
    linea.setImporteIva(BigDecimal.TEN);
    linea.setNetoGravado(BigDecimal.TEN);
    FiscalAlicuotasIva alicuota = new FiscalAlicuotasIva();
    alicuota.setFiscalCodigoAlicuota(1);
    linea.setIdAlicuotaIva(alicuota);

    dummyLibro.setFiscalLibroIvaVentasLineasList(Collections.singletonList(linea));

    return dummyLibro;
  }
}
