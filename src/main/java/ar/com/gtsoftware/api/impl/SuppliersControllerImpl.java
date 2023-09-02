package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.SuppliersController;
import ar.com.gtsoftware.api.exception.SupplierInvoiceCreateException;
import ar.com.gtsoftware.api.exception.SupplierInvoiceDeleteException;
import ar.com.gtsoftware.api.request.PaginatedSearchRequest;
import ar.com.gtsoftware.api.request.suppliers.NewSupplierInvoiceRequest;
import ar.com.gtsoftware.api.request.suppliers.SupplierInvoiceDetail;
import ar.com.gtsoftware.api.response.PaginatedResponse;
import ar.com.gtsoftware.api.response.PaginatedResponseBuilder;
import ar.com.gtsoftware.api.response.suppliers.SupplierInvoiceResponse;
import ar.com.gtsoftware.api.transformer.fromDomain.SupplierInvoiceSearchResultTransformer;
import ar.com.gtsoftware.dto.domain.FiscalAlicuotasIvaDto;
import ar.com.gtsoftware.dto.domain.FiscalLibroIvaComprasDto;
import ar.com.gtsoftware.dto.domain.FiscalLibroIvaComprasLineasDto;
import ar.com.gtsoftware.dto.domain.FiscalPeriodosFiscalesDto;
import ar.com.gtsoftware.dto.domain.NegocioTiposComprobanteDto;
import ar.com.gtsoftware.dto.domain.ProveedoresComprobantesDto;
import ar.com.gtsoftware.dto.domain.SucursalesDto;
import ar.com.gtsoftware.dto.domain.UsuariosDto;
import ar.com.gtsoftware.search.ComprobantesProveedorSearchFilter;
import ar.com.gtsoftware.service.ComprobantesProveedorService;
import ar.com.gtsoftware.service.PersonasService;
import ar.com.gtsoftware.service.exceptions.ServiceException;
import ar.com.gtsoftware.utils.SecurityUtils;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SuppliersControllerImpl implements SuppliersController {

  private final ComprobantesProveedorService proveedorService;
  private final PersonasService personasService;
  private final SecurityUtils securityUtils;
  private final PaginatedResponseBuilder responseBuilder;
  private final SupplierInvoiceSearchResultTransformer saleSearchResultTransformer;

  @Override
  // TODO add roles check from Spring
  public PaginatedResponse<SupplierInvoiceResponse> findInvoicesBySearchFilter(
      @Valid PaginatedSearchRequest<ComprobantesProveedorSearchFilter> searchRequest) {

    final ComprobantesProveedorSearchFilter searchFilter = searchRequest.getSearchFilter();
    searchFilter.addSortField("fechaComprobante", false);

    return responseBuilder.build(proveedorService, searchRequest, saleSearchResultTransformer);
  }

  @Override
  public void deleteInvoiceById(Long invoiceId) {

    ProveedoresComprobantesDto comprobante = new ProveedoresComprobantesDto();
    comprobante.setId(invoiceId);
    try {
      proveedorService.eliminarComprobante(comprobante);
    } catch (ServiceException e) {
      log.error("Error deleting supplier invoice", e);
      throw new SupplierInvoiceDeleteException(e.getMessage());
    }
  }

  @Override
  public SupplierInvoiceResponse createInvoice(NewSupplierInvoiceRequest invoiceRequest) {
    ProveedoresComprobantesDto nuevoComprobante = mapRequest(invoiceRequest);
    try {
      return saleSearchResultTransformer.transform(
          proveedorService.guardarYFiscalizar(nuevoComprobante));
    } catch (ServiceException e) {
      log.error("Error storing supplier invoice", e);
      throw new SupplierInvoiceCreateException(e.getMessage());
    }
  }

  private ProveedoresComprobantesDto mapRequest(NewSupplierInvoiceRequest invoiceRequest) {
    ProveedoresComprobantesDto nuevoComprobante = new ProveedoresComprobantesDto();
    nuevoComprobante.setFechaComprobante(invoiceRequest.invoiceDate());
    nuevoComprobante.setAnulada(false);
    nuevoComprobante.setLetra(StringUtils.upperCase(invoiceRequest.letter()));
    nuevoComprobante.setObservaciones(invoiceRequest.notes());
    nuevoComprobante.setIdUsuario(
        UsuariosDto.builder().id(securityUtils.getUserDetails().getId()).build());
    nuevoComprobante.setIdSucursal(
        SucursalesDto.builder().id(securityUtils.getUserDetails().getSucursalId()).build());
    nuevoComprobante.setIdRegistro(mapRegistro(invoiceRequest));
    nuevoComprobante.setIdProveedor(personasService.find(invoiceRequest.supplierId()));
    nuevoComprobante.setTipoComprobante(
        NegocioTiposComprobanteDto.builder().id(invoiceRequest.invoiceTypeId()).build());

    return nuevoComprobante;
  }

  private FiscalLibroIvaComprasDto mapRegistro(NewSupplierInvoiceRequest invoiceRequest) {
    FiscalLibroIvaComprasDto registro = new FiscalLibroIvaComprasDto();
    registro.setFechaFactura(invoiceRequest.invoiceDate());
    registro.setIdPeriodoFiscal(
        FiscalPeriodosFiscalesDto.builder().id(invoiceRequest.fiscalPeriodId()).build());
    registro.setPuntoVentaFactura(
        StringUtils.leftPad(invoiceRequest.pointOfSale().toString(), 4, "0"));
    registro.setNumeroFactura(
        StringUtils.leftPad(invoiceRequest.invoiceNumber().toString(), 8, "0"));

    registro.setImportePercepcionIva(invoiceRequest.taxPerceptionAmount());
    registro.setImportePercepcionIngresosBrutos(invoiceRequest.grossIncomePerceptionAmount());
    registro.setFiscalLibroIvaComprasLineasList(
        mapDetails(registro, invoiceRequest.invoiceDetails()));

    return registro;
  }

  private List<FiscalLibroIvaComprasLineasDto> mapDetails(
      FiscalLibroIvaComprasDto registro, List<SupplierInvoiceDetail> supplierInvoiceDetails) {
    List<FiscalLibroIvaComprasLineasDto> lineasDto = new ArrayList<>(supplierInvoiceDetails.size());
    int item = 1;
    for (SupplierInvoiceDetail supplierInvoiceDetail : supplierInvoiceDetails) {
      FiscalLibroIvaComprasLineasDto linea = new FiscalLibroIvaComprasLineasDto();
      linea.setItem(item++);
      linea.setImporteIva(supplierInvoiceDetail.taxAmount());
      linea.setNoGravado(supplierInvoiceDetail.nonTaxableAmount());
      linea.setNetoGravado(supplierInvoiceDetail.baseAmount());
      linea.setIdAlicuotaIva(
          FiscalAlicuotasIvaDto.builder().id(supplierInvoiceDetail.taxRateId()).build());
      linea.setIdRegistro(registro);

      lineasDto.add(linea);
    }

    return lineasDto;
  }
}
