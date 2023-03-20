package ar.com.gtsoftware.api.transformer.fromDomain;

import ar.com.gtsoftware.api.response.suppliers.SupplierInvoiceResponse;
import ar.com.gtsoftware.api.transformer.Transformer;
import ar.com.gtsoftware.dto.domain.FiscalLibroIvaComprasDto;
import ar.com.gtsoftware.dto.domain.ProveedoresComprobantesDto;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class SupplierInvoiceSearchResultTransformer
    implements Transformer<ProveedoresComprobantesDto, SupplierInvoiceResponse> {
  @Override
  public SupplierInvoiceResponse transform(ProveedoresComprobantesDto from) {
    return SupplierInvoiceResponse.builder()
        .invoiceId(from.getId())
        .total(from.getTotal())
        .invoiceDate(from.getFechaComprobante())
        .invoiceType(from.getTipoComprobante().getNombreComprobante())
        .supplier(from.getIdProveedor().getRazonSocial())
        .number(getInvoiceNumber(from))
        .build();
  }

  private String getInvoiceNumber(ProveedoresComprobantesDto from) {
    final FiscalLibroIvaComprasDto idRegistro = from.getIdRegistro();
    if (Objects.nonNull(idRegistro)) {
      return String.format(
          "%s %s-%s",
          idRegistro.getLetraFactura(),
          idRegistro.getPuntoVentaFactura(),
          idRegistro.getNumeroFactura());
    }
    return null;
  }

  @Override
  public List<SupplierInvoiceResponse> transform(List<ProveedoresComprobantesDto> from) {
    return from.stream().map(this::transform).toList();
  }
}
