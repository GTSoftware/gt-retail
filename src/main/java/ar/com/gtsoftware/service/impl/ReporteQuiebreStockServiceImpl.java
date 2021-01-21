package ar.com.gtsoftware.service.impl;

import ar.com.gtsoftware.dto.reportes.ProductoConQuiebreStock;
import ar.com.gtsoftware.dto.reportes.QuiebreStockReport;
import ar.com.gtsoftware.search.reportes.ReporteQuiebreStockSearchFilter;
import ar.com.gtsoftware.service.ReporteQuiebreStockService;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReporteQuiebreStockServiceImpl implements ReporteQuiebreStockService {

  private final EntityManager em;

  @Override
  public QuiebreStockReport obtenerReporte(ReporteQuiebreStockSearchFilter filter) {

    TypedQuery<ProductoConQuiebreStock> q =
        em.createNamedQuery("QuiebreStockQuery", ProductoConQuiebreStock.class);

    q.setParameter("fechaDesde", filter.getFechaDesde());
    q.setParameter("fechaHasta", filter.getFechaHasta());
    q.setParameter("idSucursal", filter.getIdSucursal());

    List<ProductoConQuiebreStock> items = q.getResultList();

    QuiebreStockReport report = new QuiebreStockReport(items.size());
    report.setPageRows(items);

    return report;
  }
}
