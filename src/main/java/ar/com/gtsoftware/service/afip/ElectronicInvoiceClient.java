package ar.com.gtsoftware.service.afip;

import static java.lang.Integer.parseInt;

import ar.com.gtsoftware.dto.fiscal.CAEResponse;
import ar.com.gtsoftware.entity.AFIPAuthServices;
import ar.com.gtsoftware.entity.FiscalLibroIvaVentas;
import ar.com.gtsoftware.entity.FiscalLibroIvaVentasLineas;
import ar.com.gtsoftware.enums.Parametros;
import ar.com.gtsoftware.service.ParametrosService;
import ar.com.gtsoftware.service.afip.client.fe.AlicIva;
import ar.com.gtsoftware.service.afip.client.fe.ArrayOfAlicIva;
import ar.com.gtsoftware.service.afip.client.fe.ArrayOfFECAEDetRequest;
import ar.com.gtsoftware.service.afip.client.fe.Err;
import ar.com.gtsoftware.service.afip.client.fe.FEAuthRequest;
import ar.com.gtsoftware.service.afip.client.fe.FECAECabRequest;
import ar.com.gtsoftware.service.afip.client.fe.FECAECabResponse;
import ar.com.gtsoftware.service.afip.client.fe.FECAEDetRequest;
import ar.com.gtsoftware.service.afip.client.fe.FECAEDetResponse;
import ar.com.gtsoftware.service.afip.client.fe.FECAERequest;
import ar.com.gtsoftware.service.afip.client.fe.FECAESolicitar;
import ar.com.gtsoftware.service.afip.client.fe.FECAESolicitarResponse;
import ar.com.gtsoftware.service.afip.client.fe.FECompUltimoAutorizado;
import ar.com.gtsoftware.service.afip.client.fe.FECompUltimoAutorizadoResponse;
import ar.com.gtsoftware.service.afip.client.fe.FERecuperaLastCbteResponse;
import ar.com.gtsoftware.service.afip.client.fe.Obs;
import ar.com.gtsoftware.service.afip.client.fe.Periodo;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapMessage;

@RequiredArgsConstructor
public class ElectronicInvoiceClient extends WebServiceGatewaySupport {

  private static final String CAE_SOLICITAR_SOAP_ACTION =
      "http://ar.gov.afip.dif.FEV1/FECAESolicitar";
  private static final String ULTIMO_AUTORIZADO_SOAP_ACTION =
      "http://ar.gov.afip.dif.FEV1/FECompUltimoAutorizado";
  private static final String SUCCESS_RESULT = "A";
  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyyMMdd");
  private final Logger logger = LoggerFactory.getLogger(ElectronicInvoiceClient.class);

  private final ParametrosService parametrosService;

  public int getLastAuthorizedInvoiceNumber(
      AFIPAuthServices authToken, int pointOfSale, int invoiceType) {

    FECompUltimoAutorizado lastAuthorizedRequest = new FECompUltimoAutorizado();
    lastAuthorizedRequest.setCbteTipo(invoiceType);
    lastAuthorizedRequest.setPtoVta(pointOfSale);
    lastAuthorizedRequest.setAuth(transformAuth(authToken));

    logger.info(
        "Solicitando último comprobante autorizado - Tipo: {} Punto de venta: {}",
        invoiceType,
        pointOfSale);

    final Object soapResponse =
        getWebServiceTemplate()
            .marshalSendAndReceive(
                lastAuthorizedRequest,
                message -> ((SoapMessage) message).setSoapAction(ULTIMO_AUTORIZADO_SOAP_ACTION));

    FECompUltimoAutorizadoResponse lastInvoice = (FECompUltimoAutorizadoResponse) soapResponse;
    final FERecuperaLastCbteResponse ultimoAutorizadoResult =
        lastInvoice.getFECompUltimoAutorizadoResult();

    final int cbteNro = ultimoAutorizadoResult.getCbteNro();

    if (Objects.nonNull(ultimoAutorizadoResult.getErrors())) {
      StringBuilder sb = new StringBuilder("Errores solicitando último autorizado");
      for (Err err : ultimoAutorizadoResult.getErrors().getErr()) {
        sb.append(String.format("\nCode: %s Msg: %s", err.getCode(), err.getMsg()));
      }
      final String errorMsg = sb.toString();
      logger.error(errorMsg);

      throw new RuntimeException(errorMsg);
    }

    logger.info("Último comprobante autorizado - Número: {}", cbteNro);

    return cbteNro;
  }

  private FEAuthRequest transformAuth(AFIPAuthServices authToken) {
    FEAuthRequest authRequest = new FEAuthRequest();
    authRequest.setToken(authToken.getToken());
    authRequest.setSign(authToken.getSign());
    authRequest.setCuit(parametrosService.getLongParam(Parametros.EMPRESA_CUIT));

    return authRequest;
  }

  public CAEResponse requestElectronicAuthorization(
      AFIPAuthServices authToken, FiscalLibroIvaVentas registro) {
    FECAESolicitar solicitarRequest = new FECAESolicitar();
    solicitarRequest.setAuth(transformAuth(authToken));

    FECAERequest caeRequest = transformCaeRequest(registro);

    solicitarRequest.setFeCAEReq(caeRequest);
    logger.info(
        "Solicitando CAE - PV: {} Nro: {} Documento: {}",
        registro.getPuntoVentaFactura(),
        registro.getNumeroFactura(),
        registro.getDocumento());

    final Object soapResponse =
        getWebServiceTemplate()
            .marshalSendAndReceive(
                solicitarRequest,
                message -> ((SoapMessage) message).setSoapAction(CAE_SOLICITAR_SOAP_ACTION));

    FECAESolicitarResponse caeResponse = (FECAESolicitarResponse) soapResponse;
    final FECAECabResponse cabResp = caeResponse.getFECAESolicitarResult().getFeCabResp();

    logger.info("CAE Solicitado");

    if (Objects.nonNull(cabResp) && StringUtils.equals(SUCCESS_RESULT, cabResp.getResultado())) {
      final FECAEDetResponse fecaeDetResponse =
          caeResponse.getFECAESolicitarResult().getFeDetResp().getFECAEDetResponse().get(0);

      final long cae = Long.parseLong(fecaeDetResponse.getCAE());
      final LocalDate fechaVencimientoCae =
          LocalDate.parse(fecaeDetResponse.getCAEFchVto(), DATE_TIME_FORMATTER);

      return new CAEResponse(cae, fechaVencimientoCae);
    }
    final String errorMessage = getErrorMessageFromResponse(caeResponse);
    logger.error("Error solicitando CAE {}", errorMessage);

    throw new RuntimeException(errorMessage);
  }

  private String getErrorMessageFromResponse(FECAESolicitarResponse caeReponse) {
    StringBuilder sb = new StringBuilder();
    if (Objects.nonNull(caeReponse.getFECAESolicitarResult().getErrors())) {
      for (Err err : caeReponse.getFECAESolicitarResult().getErrors().getErr()) {
        sb.append("Código=")
            .append(err.getCode())
            .append(StringUtils.SPACE)
            .append("Mensaje=")
            .append(err.getMsg());
      }
    }

    for (FECAEDetResponse detResponse :
        caeReponse.getFECAESolicitarResult().getFeDetResp().getFECAEDetResponse()) {
      sb.append("Resultado=").append(detResponse.getResultado()).append(StringUtils.LF);
      sb.append("Observaciones=" + StringUtils.LF);
      if (Objects.nonNull(detResponse.getObservaciones())) {
        for (Obs obs : detResponse.getObservaciones().getObs()) {
          sb.append("Código=")
              .append(obs.getCode())
              .append(StringUtils.SPACE)
              .append("Mensaje=")
              .append(obs.getMsg());
        }
      }
    }

    return sb.toString();
  }

  private FECAERequest transformCaeRequest(FiscalLibroIvaVentas registro) {
    FECAERequest caeRequest = new FECAERequest();

    final FECAECabRequest cabRequest = new FECAECabRequest();
    cabRequest.setCantReg(1);
    cabRequest.setCbteTipo(
        parseInt(registro.getCodigoTipoComprobante().getCodigoTipoComprobante()));
    cabRequest.setPtoVta(parseInt(registro.getPuntoVentaFactura()));

    caeRequest.setFeCabReq(cabRequest);

    final ArrayOfFECAEDetRequest arrayDetRequest = new ArrayOfFECAEDetRequest();
    final FECAEDetRequest detRequest = new FECAEDetRequest();

    detRequest.setConcepto(3); // Hardcoded
    detRequest.setDocTipo(
        registro.getIdPersona().getIdTipoDocumento().getFiscalCodigoTipoDocumento());
    detRequest.setDocNro(Long.parseLong(registro.getDocumento()));
    detRequest.setCbteDesde(Long.parseLong(registro.getNumeroFactura()));
    detRequest.setCbteHasta(Long.parseLong(registro.getNumeroFactura()));
    final String formattedInvoiceDate = DATE_TIME_FORMATTER.format(registro.getFechaFactura());
    detRequest.setCbteFch(formattedInvoiceDate);
    detRequest.setImpTotal(registro.getTotalFactura().abs().doubleValue());
    detRequest.setImpTotConc(0);

    final ArrayOfAlicIva arrayOfAlicIva = new ArrayOfAlicIva();
    for (FiscalLibroIvaVentasLineas lineaIva : registro.getFiscalLibroIvaVentasLineasList()) {
      AlicIva alicIva = new AlicIva();
      alicIva.setId(lineaIva.getIdAlicuotaIva().getFiscalCodigoAlicuota());
      alicIva.setBaseImp(lineaIva.getNetoGravado().abs().doubleValue());
      alicIva.setImporte(lineaIva.getImporteIva().abs().doubleValue());

      arrayOfAlicIva.getAlicIva().add(alicIva);
    }
    detRequest.setIva(arrayOfAlicIva);

    detRequest.setImpNeto(registro.getImporteNetoGravado().abs().doubleValue());
    detRequest.setImpOpEx(registro.getImporteExento().abs().doubleValue());
    detRequest.setImpIVA(registro.getImporteIva().abs().doubleValue());
    detRequest.setImpTrib(registro.getImporteTributos().abs().doubleValue());

    detRequest.setFchServDesde(formattedInvoiceDate);
    detRequest.setFchServHasta(formattedInvoiceDate);
    detRequest.setFchVtoPago(formattedInvoiceDate);

    detRequest.setMonId("PES");
    detRequest.setMonCotiz(1);

    if (isDebitoCredito(registro)) {
      Periodo periodoAsoc = new Periodo();
      periodoAsoc.setFchDesde(formattedInvoiceDate);
      periodoAsoc.setFchHasta(formattedInvoiceDate);
      detRequest.setPeriodoAsoc(periodoAsoc);
    }

    arrayDetRequest.getFECAEDetRequest().add(detRequest);
    caeRequest.setFeDetReq(arrayDetRequest);

    return caeRequest;
  }

  private boolean isDebitoCredito(FiscalLibroIvaVentas registro) {
    final Long idTipoComp = registro.getCodigoTipoComprobante().getTipoComprobante().getId();

    return idTipoComp == 2 || idTipoComp == 3;
  }

  protected ParametrosService getParametrosService() {
    return parametrosService;
  }
}
