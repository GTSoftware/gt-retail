package ar.com.gtsoftware.api.transformer.fromDomain;

import ar.com.gtsoftware.api.response.PaymentPendingSale;
import ar.com.gtsoftware.api.transformer.Transformer;
import ar.com.gtsoftware.dto.domain.ComprobantesDto;
import ar.com.gtsoftware.dto.domain.FiscalLibroIvaVentasDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class PaymentPendingSaleTransformer
    implements Transformer<ComprobantesDto, PaymentPendingSale> {

  @Override
  public List<PaymentPendingSale> transform(List<ComprobantesDto> comprobantesDtos) {
    List<PaymentPendingSale> results = new ArrayList<>(comprobantesDtos.size());
    for (ComprobantesDto comprobantesDto : comprobantesDtos) {
      results.add(transform(comprobantesDto));
    }
    return results;
  }

  @Override
  public PaymentPendingSale transform(ComprobantesDto comprobantesDto) {
    return PaymentPendingSale.builder()
        .saleId(comprobantesDto.getId())
        .branch(comprobantesDto.getIdSucursal().getNombreSucursal())
        .remainingAmount(comprobantesDto.getSaldo())
        .total(comprobantesDto.getTotal())
        .saleDate(comprobantesDto.getFechaComprobante())
        .user(comprobantesDto.getIdUsuario().getNombreUsuario())
        .saleType(comprobantesDto.getTipoComprobante().getNombreComprobante())
        .saleCondition(comprobantesDto.getIdCondicionComprobante().getNombreCondicion())
        .customer(comprobantesDto.getIdPersona().getRazonSocial())
        .customerId(comprobantesDto.getIdPersona().getId())
        .invoiceNumber(transformInvoiceNumber(comprobantesDto))
        .build();
  }

  private String transformInvoiceNumber(ComprobantesDto comprobantesDto) {
    final FiscalLibroIvaVentasDto regFiscal = comprobantesDto.getIdRegistro();
    if (Objects.nonNull(regFiscal)) {
      return String.format(
          "%s %s-%s",
          comprobantesDto.getLetra(),
          regFiscal.getPuntoVentaFactura(),
          regFiscal.getNumeroFactura());
    }

    return null;
  }
}
