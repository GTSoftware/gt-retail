/*
 * Copyright 2019 GT Software.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package ar.com.gtsoftware.service.impl;

import ar.com.gtsoftware.dto.reportes.VentaPorProducto;
import ar.com.gtsoftware.dto.reportes.VentaPorProductoReport;
import ar.com.gtsoftware.search.reportes.ReporteVentasSearchFilter;
import ar.com.gtsoftware.service.ReportesVentaService;
import ar.com.gtsoftware.utils.BusinessDateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReportesVentaServiceImpl implements ReportesVentaService {

    private final EntityManager em;
    private final BusinessDateUtils dateUtils;

    @Override
    public VentaPorProductoReport obtenerReporte(ReporteVentasSearchFilter filter) {
        TypedQuery<BigInteger> qCount = (TypedQuery<BigInteger>) em.createNamedQuery("ReporteVentasQueryCount");
        if (!filter.hasFechasFilter()) {
            setDefaultDates(filter);
        }
        long idSucursalFilter = filter.getIdSucursal() == null ? -1 : filter.getIdSucursal();

        qCount.setParameter("fechaDesde", filter.getFechaDesde());
        qCount.setParameter("fechaHasta", filter.getFechaHasta());
        qCount.setParameter("idSucursal", idSucursalFilter);

        int maxRows = qCount.getSingleResult().intValue();

        VentaPorProductoReport report = new VentaPorProductoReport(maxRows);

        TypedQuery<VentaPorProducto> q = em.createNamedQuery("ReporteVentasQuery", VentaPorProducto.class);
        q.setMaxResults(filter.getPageSize());
        q.setFirstResult(filter.getFirstRow());

        q.setParameter("fechaDesde", filter.getFechaDesde());
        q.setParameter("fechaHasta", filter.getFechaHasta());
        q.setParameter("idSucursal", idSucursalFilter);

        List<VentaPorProducto> items = q.getResultList();

        report.setPageRows(items);

        return report;
    }

    private void setDefaultDates(ReporteVentasSearchFilter filter) {
        filter.setFechaDesde(dateUtils.getStartDateOfCurrentMonth());
        filter.setFechaHasta(dateUtils.getEndDateOfCurrentMonth());
    }
}
