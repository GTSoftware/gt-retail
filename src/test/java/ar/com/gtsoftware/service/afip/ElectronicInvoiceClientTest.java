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
import java.util.List;
import javax.xml.transform.Source;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ws.test.client.MockWebServiceServer;
import org.springframework.xml.transform.StringSource;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = AfipClientTestConfiguration.class)
class ElectronicInvoiceClientTest {

  @Autowired private ElectronicInvoiceClient client;

  private MockWebServiceServer mockServer;

  @Spy private NegocioTiposComprobante negocioTipoComprobante;
  @Spy private FiscalLibroIvaVentas registroVenta;
  @Spy private FiscalTiposComprobante fiscalTipoComprobante;
  @Spy private Personas persona;
  @Spy private LegalTiposDocumento tipoDocumento;
  @Spy private FiscalLibroIvaVentasLineas lineaRegistro;
  @Spy private FiscalAlicuotasIva alicuotaIva;

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
            "<ns2:FECompUltimoAutorizado xmlns:ns2=\"http://ar.gov.afip.dif.FEV1/\">"
                + "<ns2:Auth>"
                + "<ns2:Token>token</ns2:Token>"
                + "<ns2:Sign>sign</ns2:Sign>"
                + "<ns2:Cuit>99999999999</ns2:Cuit>"
                + "</ns2:Auth><ns2:PtoVta>1</ns2:PtoVta>"
                + "<ns2:CbteTipo>1</ns2:CbteTipo>"
                + "</ns2:FECompUltimoAutorizado>");

    Source responsePayload =
        new StringSource(
            "<FECompUltimoAutorizadoResponse xmlns=\"http://ar.gov.afip.dif.FEV1/\">\n"
                + "    <FECompUltimoAutorizadoResult>\n"
                + "        <PtoVta>1</PtoVta>\n"
                + "        <CbteTipo>1</CbteTipo>\n"
                + "        <CbteNro>14</CbteNro>\n"
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
  void shouldThrowRuntimeExceptionWhenGetLastAuthorizedInvoiceNumberHasErrors() {
    final AFIPAuthServices afipAuthServices = new AFIPAuthServices();
    afipAuthServices.setSign("sign");
    afipAuthServices.setToken("token");

    Source requestPayload =
        new StringSource(
            "<ns2:FECompUltimoAutorizado xmlns:ns2=\"http://ar.gov.afip.dif.FEV1/\">"
                + "<ns2:Auth><ns2:Token>token</ns2:Token>"
                + "<ns2:Sign>sign</ns2:Sign>"
                + "<ns2:Cuit>99999999999</ns2:Cuit>"
                + "</ns2:Auth>"
                + "<ns2:PtoVta>1</ns2:PtoVta>"
                + "<ns2:CbteTipo>1</ns2:CbteTipo>"
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
                + "                <Code>005</Code>\n"
                + "                <Msg>Some error</Msg>\n"
                + "            </Err>\n"
                + "            <Err>\n"
                + "                <Code>006</Code>\n"
                + "                <Msg>Other error</Msg>\n"
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

    Assertions.assertThrows(
        RuntimeException.class,
        () -> client.getLastAuthorizedInvoiceNumber(afipAuthServices, 1, 1));

    mockServer.verify();
  }

  @Test
  void shouldRequestElectronicAuthorization() {
    setUpMocks();

    final AFIPAuthServices afipAuthServices = new AFIPAuthServices();
    afipAuthServices.setSign("sign");
    afipAuthServices.setToken("token");

    Source requestPayload =
        new StringSource(
            "<ns2:FECAESolicitar xmlns:ns2=\"http://ar.gov.afip.dif.FEV1/\">"
                + "<ns2:Auth>"
                + "<ns2:Token>token</ns2:Token>"
                + "<ns2:Sign>sign</ns2:Sign>"
                + "<ns2:Cuit>99999999999</ns2:Cuit>"
                + "</ns2:Auth>"
                + "<ns2:FeCAEReq>"
                + "<ns2:FeCabReq>"
                + "<ns2:CantReg>1</ns2:CantReg>"
                + "<ns2:PtoVta>1</ns2:PtoVta>"
                + "<ns2:CbteTipo>99</ns2:CbteTipo>"
                + "</ns2:FeCabReq><ns2:FeDetReq>"
                + "<ns2:FECAEDetRequest>"
                + "<ns2:Concepto>3</ns2:Concepto>"
                + "<ns2:DocTipo>99</ns2:DocTipo>"
                + "<ns2:DocNro>88888888</ns2:DocNro>"
                + "<ns2:CbteDesde>1</ns2:CbteDesde>"
                + "<ns2:CbteHasta>1</ns2:CbteHasta>"
                + "<ns2:CbteFch>20200101</ns2:CbteFch>"
                + "<ns2:ImpTotal>10.53</ns2:ImpTotal>"
                + "<ns2:ImpTotConc>0.0</ns2:ImpTotConc>"
                + "<ns2:ImpNeto>10.0</ns2:ImpNeto>"
                + "<ns2:ImpOpEx>10.0</ns2:ImpOpEx>"
                + "<ns2:ImpTrib>10.0</ns2:ImpTrib>"
                + "<ns2:ImpIVA>10.0</ns2:ImpIVA>"
                + "<ns2:FchServDesde>20200101</ns2:FchServDesde>"
                + "<ns2:FchServHasta>20200101</ns2:FchServHasta>"
                + "<ns2:FchVtoPago>20200101</ns2:FchVtoPago>"
                + "<ns2:MonId>PES</ns2:MonId>"
                + "<ns2:MonCotiz>1.0</ns2:MonCotiz><ns2:Iva>"
                + "<ns2:AlicIva><ns2:Id>1</ns2:Id>"
                + "<ns2:BaseImp>10.0</ns2:BaseImp>"
                + "<ns2:Importe>10.0</ns2:Importe>"
                + "</ns2:AlicIva></ns2:Iva>"
                + "</ns2:FECAEDetRequest>"
                + "</ns2:FeDetReq>"
                + "</ns2:FeCAEReq><"
                + "/ns2:FECAESolicitar>");

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
        client.requestElectronicAuthorization(afipAuthServices, registroVenta);

    assertThat(caeResponse.getCae(), is(1234L));
    assertThat(caeResponse.getFechaVencimientoCae(), is(LocalDate.of(2020, 1, 6)));
    mockServer.verify();
  }

  private void setUpMocks() {
    when(registroVenta.getDocumento()).thenReturn("88888888");
    when(registroVenta.getNumeroFactura()).thenReturn("1");
    when(registroVenta.getFechaFactura()).thenReturn(LocalDateTime.of(2020, 1, 1, 13, 21));
    when(registroVenta.getImporteExento()).thenReturn(BigDecimal.TEN);
    when(registroVenta.getImporteIva()).thenReturn(BigDecimal.TEN);
    when(registroVenta.getImporteNetoGravado()).thenReturn(BigDecimal.TEN);
    when(registroVenta.getImporteTributos()).thenReturn(BigDecimal.TEN);
    when(registroVenta.getImporteNetoNoGravado()).thenReturn(BigDecimal.TEN);
    when(registroVenta.getTotalFactura()).thenReturn(BigDecimal.valueOf(10.53));
    when(registroVenta.getPuntoVentaFactura()).thenReturn("1");
    when(registroVenta.getCodigoTipoComprobante()).thenReturn(fiscalTipoComprobante);

    when(registroVenta.getIdPersona()).thenReturn(persona);
    when(persona.getIdTipoDocumento()).thenReturn(tipoDocumento);
    when(tipoDocumento.getFiscalCodigoTipoDocumento()).thenReturn(99);

    when(fiscalTipoComprobante.getCodigoTipoComprobante()).thenReturn("99");
    when(fiscalTipoComprobante.getTipoComprobante()).thenReturn(negocioTipoComprobante);
    when(negocioTipoComprobante.getId()).thenReturn(1L);

    when(registroVenta.getFiscalLibroIvaVentasLineasList()).thenReturn(List.of(lineaRegistro));

    when(lineaRegistro.getNoGravado()).thenReturn(BigDecimal.TEN);
    when(lineaRegistro.getImporteIva()).thenReturn(BigDecimal.TEN);
    when(lineaRegistro.getNetoGravado()).thenReturn(BigDecimal.TEN);
    when(lineaRegistro.getIdAlicuotaIva()).thenReturn(alicuotaIva);
    when(alicuotaIva.getFiscalCodigoAlicuota()).thenReturn(1);
  }
}
