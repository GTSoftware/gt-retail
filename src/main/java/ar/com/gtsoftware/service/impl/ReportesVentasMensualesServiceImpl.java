package ar.com.gtsoftware.service.impl;

import ar.com.gtsoftware.dto.reportes.VentaMensual;
import ar.com.gtsoftware.dto.reportes.VentasMensualesReport;
import ar.com.gtsoftware.search.reportes.ReporteVentasMensualesSearchFilter;
import ar.com.gtsoftware.service.ReportesVentasMensualesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReportesVentasMensualesServiceImpl implements ReportesVentasMensualesService {

    private final EntityManager em;

    @Override
    public VentasMensualesReport obtenerReporte(ReporteVentasMensualesSearchFilter filter) {
        TypedQuery<BigInteger> qCount = (TypedQuery<BigInteger>) em.createNamedQuery("VentasMensualesQueryCount");

        qCount.setParameter("fechaDesde", filter.getFechaDesde());
        qCount.setParameter("fechaHasta", filter.getFechaHasta());

        int maxRows = qCount.getSingleResult().intValue();

        VentasMensualesReport report = new VentasMensualesReport(maxRows);

        TypedQuery<VentaMensual> q = em.createNamedQuery("VentasMensualesQuery", VentaMensual.class);

        q.setParameter("fechaDesde", filter.getFechaDesde());
        q.setParameter("fechaHasta", filter.getFechaHasta());

        q.setMaxResults(filter.getPageSize());
        q.setFirstResult(filter.getFirstRow());

        List<VentaMensual> items = q.getResultList();

        report.setPageRows(items);

        return report;
    }
}
