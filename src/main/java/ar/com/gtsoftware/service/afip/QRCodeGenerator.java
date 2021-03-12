package ar.com.gtsoftware.service.afip;

import ar.com.gtsoftware.domain.FiscalLibroIvaVentas;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Objects;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class QRCodeGenerator {

  private static final String AFIP_URL = "https://www.afip.gob.ar/fe/qr/?p=";
  private static final Integer JSON_VERSION = 1;
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  public static final String MONEDA = "PES";
  public static final String TIPO_CODIGO_AUTORIZACION = "E";

  public String generarCodigo(FiscalLibroIvaVentas idRegistro, Long cuitEmpresa) {
    if (Objects.isNull(idRegistro.getCae())) {
      return StringUtils.EMPTY;
    }

    final QRInfo qrInfo = mapQRInfo(idRegistro, cuitEmpresa);
    Base64.Encoder encoder = Base64.getEncoder();

    return AFIP_URL + encoder.encodeToString(qrInfo.toString().getBytes());
  }

  private QRInfo mapQRInfo(FiscalLibroIvaVentas idRegistro, Long cuitEmpresa) {
    return QRInfo.builder()
        .ver(JSON_VERSION)
        .fecha(FORMATTER.format(idRegistro.getFechaFactura()))
        .cuit(cuitEmpresa)
        .ptoVta(Integer.parseInt(idRegistro.getPuntoVentaFactura()))
        .tipoCmp(Integer.parseInt(idRegistro.getCodigoTipoComprobante().getCodigoTipoComprobante()))
        .nroCmp(Integer.parseInt(idRegistro.getNumeroFactura()))
        .importe(idRegistro.getTotalFactura())
        .moneda(MONEDA)
        .ctz(BigDecimal.ONE)
        .tipoDocRec(idRegistro.getIdPersona().getIdTipoDocumento().getFiscalCodigoTipoDocumento())
        .nroDocRec(Long.parseLong(idRegistro.getDocumento()))
        .tipoCodAut(TIPO_CODIGO_AUTORIZACION)
        .codAut(idRegistro.getCae())
        .build();
  }

  @Builder
  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  private static class QRInfo {
    private Integer ver;
    private String fecha;
    private Long cuit;
    private Integer ptoVta;
    private Integer tipoCmp;
    private Integer nroCmp;
    private BigDecimal importe;
    private String moneda;
    private BigDecimal ctz;
    private Integer tipoDocRec;
    private Long nroDocRec;
    private String tipoCodAut;
    private Long codAut;

    @Override
    public String toString() {
      ObjectMapper objectMapper = new ObjectMapper();
      try {
        return objectMapper.writeValueAsString(this);
      } catch (JsonProcessingException e) {
        return StringUtils.EMPTY;
      }
    }
  }
}
