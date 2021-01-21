package ar.com.gtsoftware.api.impl;

import static org.apache.commons.lang3.StringUtils.leftPad;

import ar.com.gtsoftware.api.DashboardController;
import ar.com.gtsoftware.api.response.ProductStockStatus;
import ar.com.gtsoftware.api.response.SaleReport;
import ar.com.gtsoftware.api.response.StockBreak;
import ar.com.gtsoftware.dto.reportes.ProductoConQuiebreStock;
import ar.com.gtsoftware.dto.reportes.QuiebreStockReport;
import ar.com.gtsoftware.dto.reportes.VentaMensual;
import ar.com.gtsoftware.dto.reportes.VentasMensualesReport;
import ar.com.gtsoftware.search.ComprobantesSearchFilter;
import ar.com.gtsoftware.search.PersonasSearchFilter;
import ar.com.gtsoftware.search.reportes.ReporteQuiebreStockSearchFilter;
import ar.com.gtsoftware.search.reportes.ReporteVentasMensualesSearchFilter;
import ar.com.gtsoftware.service.ComprobantesService;
import ar.com.gtsoftware.service.PersonasService;
import ar.com.gtsoftware.service.ReporteQuiebreStockService;
import ar.com.gtsoftware.service.ReportesVentasMensualesService;
import ar.com.gtsoftware.utils.BusinessDateUtils;
import ar.com.gtsoftware.utils.SecurityUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DashboardControllerImpl implements DashboardController {

  private final ComprobantesService comprobantesService;
  private final PersonasService personasService;
  private final BusinessDateUtils dateUtils;
  private final SecurityUtils securityUtils;
  private final ReportesVentasMensualesService ventasMensualesService;
  private final ReporteQuiebreStockService quiebreStockService;

  @Override
  public Integer getSalesQuantity() {

    final ComprobantesSearchFilter sf =
        ComprobantesSearchFilter.builder()
            .fechaVentaDesde(dateUtils.getStartDateTimeOfCurrentMonth())
            .fechaVentaHasta(dateUtils.getEndDateTimeOfCurrentMonth())
            .idTiposComprobanteList(Collections.singletonList(1L))
            .anulada(false)
            .idSucursal(securityUtils.getUserDetails().getSucursalId())
            .build();

    return comprobantesService.countBySearchFilter(sf);
  }

  @Override
  public Integer getNewCustomersQuantity() {
    final PersonasSearchFilter sf =
        PersonasSearchFilter.builder()
            .activo(true)
            .fechaAltaDesde(dateUtils.getStartDateTimeOfCurrentMonth())
            .fechaAltaHasta(dateUtils.getEndDateTimeOfCurrentMonth())
            .cliente(true)
            .build();

    return personasService.countBySearchFilter(sf);
  }

  @Override
  public List<SaleReport> getMonthlySalesReport() {
    final ReporteVentasMensualesSearchFilter sf =
        ReporteVentasMensualesSearchFilter.builder()
            .fechaDesde(dateUtils.getStartDateOfCurrentMonth().minusMonths(12))
            .fechaHasta(dateUtils.getEndDateOfCurrentMonth())
            .build();

    final VentasMensualesReport ventasMensualesReport = ventasMensualesService.obtenerReporte(sf);

    return transformVentasMensualesReport(ventasMensualesReport);
  }

  @Override
  public List<StockBreak> getProductsWithStockBreak() {
    final ReporteQuiebreStockSearchFilter sf =
        ReporteQuiebreStockSearchFilter.builder()
            .fechaDesde(dateUtils.getStartDateOfCurrentMonth())
            .fechaHasta(dateUtils.getEndDateOfCurrentMonth())
            .idSucursal(securityUtils.getUserDetails().getSucursalId())
            .build();
    final QuiebreStockReport quiebreStockReport = quiebreStockService.obtenerReporte(sf);

    return transformQuiebreStockReport(quiebreStockReport);
  }

  private List<StockBreak> transformQuiebreStockReport(QuiebreStockReport quiebreStockReport) {
    List<StockBreak> stockBreakList = new ArrayList<>(quiebreStockReport.getTotalRows());
    for (ProductoConQuiebreStock pq : quiebreStockReport.getPageRows()) {
      stockBreakList.add(
          StockBreak.builder()
              .productCode(pq.getCodigoPropio())
              .description(pq.getDescripcion())
              .minimumStock(pq.getStockMinimo())
              .branchStock(pq.getStockSucursal())
              .saleUnit(pq.getUnidadVenta())
              .status(getProductStatus(pq.getStockMinimo(), pq.getStockSucursal()))
              .build());
    }

    return stockBreakList;
  }

  private ProductStockStatus getProductStatus(BigDecimal stockMinimo, BigDecimal stockSucursal) {
    ProductStockStatus status = ProductStockStatus.OUT_OF_STOCK;

    if (stockMinimo.signum() > 0 && stockSucursal.signum() > 0) {
      if (stockMinimo.equals(stockSucursal)) {
        status = ProductStockStatus.AT_MINIMUM;
      } else if (stockSucursal.compareTo(stockMinimo) < 0) {
        status = ProductStockStatus.BELOW_MINIMUM;
      }
    }

    return status;
  }

  private List<SaleReport> transformVentasMensualesReport(
      VentasMensualesReport ventasMensualesReport) {
    List<SaleReport> saleReportList = new ArrayList<>(ventasMensualesReport.getTotalRows());
    for (VentaMensual vm : ventasMensualesReport.getPageRows()) {
      saleReportList.add(
          SaleReport.builder()
              .period(String.format("%d-%s", vm.getAnio(), leftPad(vm.getMes().toString(), 2, "0")))
              .amount(vm.getTotal())
              .build());
    }

    return saleReportList;
  }
}
