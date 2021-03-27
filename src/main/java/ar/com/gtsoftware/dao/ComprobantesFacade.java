/*
 * Copyright 2014 GT Software.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ar.com.gtsoftware.dao;

import ar.com.gtsoftware.entity.*;
import ar.com.gtsoftware.search.ComprobantesSearchFilter;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class ComprobantesFacade extends AbstractFacade<Comprobantes, ComprobantesSearchFilter> {

  private final EntityManager em;

  public ComprobantesFacade(EntityManager em) {
    super(Comprobantes.class);
    this.em = em;
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  @Override
  protected Predicate createWhereFromSearchFilter(
      ComprobantesSearchFilter vsf, CriteriaBuilder cb, Root<Comprobantes> root) {

    Predicate p = null;
    if (vsf.getIdVenta() != null) {
      p = cb.equal(root.get(Comprobantes_.id), vsf.getIdVenta());
    }
    if (vsf.getFechaVentaDesde() != null && vsf.getFechaVentaHasta() != null) {
      Predicate p1 =
          cb.between(
              root.get(Comprobantes_.fechaComprobante),
              vsf.getFechaVentaDesde(),
              vsf.getFechaVentaHasta());
      p = appendAndPredicate(cb, p1, p);
    }
    if (vsf.getIdPersona() != null) {
      Predicate p1 =
          cb.equal(root.get(Comprobantes_.idPersona).get(Personas_.id), vsf.getIdPersona());
      p = appendAndPredicate(cb, p1, p);
    }
    if (vsf.getIdSucursal() != null) {
      Predicate p1 =
          cb.equal(root.get(Comprobantes_.idSucursal).get(Sucursales_.id), vsf.getIdSucursal());
      p = appendAndPredicate(cb, p1, p);
    }
    if (vsf.getIdCondicionVenta() != null) {
      Predicate p1 =
          cb.equal(
              root.get(Comprobantes_.idCondicionComprobante).get(NegocioCondicionesOperaciones_.id),
              vsf.getIdCondicionVenta());
      p = appendAndPredicate(cb, p1, p);
    }
    if (vsf.getIdUsuario() != null) {
      Predicate p1 =
          cb.equal(root.get(Comprobantes_.idUsuario).get(Usuarios_.id), vsf.getIdUsuario());
      p = appendAndPredicate(cb, p1, p);
    }
    if (vsf.getFacturada() != null) {
      Predicate p1;
      if (vsf.getFacturada()) {
        p1 = cb.isNotNull(root.get(Comprobantes_.idRegistro));
      } else {
        p1 = cb.isNull(root.get(Comprobantes_.idRegistro));
      }

      p = appendAndPredicate(cb, p1, p);
    }
    if (vsf.getConSaldo() != null) {
      Predicate p1;
      if (vsf.getConSaldo()) {
        p1 = cb.greaterThan(root.get(Comprobantes_.saldo), BigDecimal.ZERO);
      } else {
        p1 = cb.equal(root.get(Comprobantes_.saldo), BigDecimal.ZERO);
      }

      p = appendAndPredicate(cb, p1, p);
    }
    if (vsf.getAnulada() != null) {
      Predicate p1;
      if (vsf.getAnulada()) {
        p1 = cb.isTrue(root.get(Comprobantes_.anulada));
      } else {
        p1 = cb.isFalse(root.get(Comprobantes_.anulada));
      }

      p = appendAndPredicate(cb, p1, p);
    }
    if (StringUtils.isNotEmpty(vsf.getNumeroFactura())) {
      Predicate p1;
      Expression<String> nroFactura =
          cb.concat(
              root.get(Comprobantes_.idRegistro).get(FiscalLibroIvaVentas_.letraFactura),
              root.get(Comprobantes_.idRegistro).get(FiscalLibroIvaVentas_.puntoVentaFactura));
      nroFactura =
          cb.concat(
              nroFactura,
              root.get(Comprobantes_.idRegistro).get(FiscalLibroIvaVentas_.numeroFactura));
      p1 = cb.like(nroFactura, String.format("%%%s%%", vsf.getNumeroFactura()));

      p = appendAndPredicate(cb, p1, p);
    }
    if (vsf.hasTiposComprobanteFilter()) {
      Predicate p1 =
          root.get(Comprobantes_.tipoComprobante)
              .get(NegocioTiposComprobante_.id)
              .in(vsf.getIdTiposComprobanteList());
      p = appendAndPredicate(cb, p1, p);
    }
    return p;
  }

  /**
   * Obtiene el total de ventas dado el filtro
   *
   * @param sf
   * @return
   */
  public BigDecimal calcularTotalVentasBySearchFilter(ComprobantesSearchFilter sf) {
    if (sf.hasFilter()) {
      CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
      CriteriaQuery<BigDecimal> cq = cb.createQuery(BigDecimal.class);
      Root<Comprobantes> root = cq.from(Comprobantes.class);
      cq.select(
          cb.coalesce(
              cb.sum(
                  cb.prod(
                      root.get(Comprobantes_.total),
                      root.get(Comprobantes_.tipoComprobante).get(NegocioTiposComprobante_.signo))),
              cb.literal(BigDecimal.ZERO)));
      Predicate p = createWhereFromSearchFilter(sf, cb, root);
      cq.where(p);
      TypedQuery<BigDecimal> q = getEntityManager().createQuery(cq);
      return q.getSingleResult();
    }
    return BigDecimal.ZERO;
  }
  //
  //    /**
  //     * Obtiene el total de ventas facturadas
  //     *
  //     * @param sf
  //     * @return
  //     */
  //    public BigDecimal calcularTotalVentasFacturadasBySearchFilter(ComprobantesSearchFilter sf) {
  //        if (sf.hasFilter()) {
  //            CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
  //            CriteriaQuery<BigDecimal> cq = cb.createQuery(BigDecimal.class);
  //            Root<Comprobantes> root = cq.from(Comprobantes.class);
  //            cq.select(cb.sum(cb.prod(root.get(Comprobantes_.total),
  //
  // root.get(Comprobantes_.tipoComprobante).get(NegocioTiposComprobante_.signo))));
  //            Predicate p = createWhereFromSearchFilter(sf, cb, root);
  //            p = appendAndPredicate(cb, cb.not(cb.isNull(root.get(Comprobantes_.idRegistro))),
  // p);
  //            cq.where(p);
  //            TypedQuery<BigDecimal> q = getEntityManager().createQuery(cq);
  //            return q.getSingleResult();
  //
  //        }
  //        return BigDecimal.ZERO;
  //    }
  //
  //    /**
  //     * Obtiene el total de ventas sin facturar
  //     *
  //     * @param sf
  //     * @return
  //     */
  //    public BigDecimal calcularTotalVentasSinFacturarBySearchFilter(ComprobantesSearchFilter sf)
  // {
  //        if (sf.hasFilter()) {
  //            CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
  //            CriteriaQuery<BigDecimal> cq = cb.createQuery(BigDecimal.class);
  //            Root<Comprobantes> root = cq.from(Comprobantes.class);
  //            cq.select(cb.sum(cb.prod(root.get(Comprobantes_.total),
  //
  // root.get(Comprobantes_.tipoComprobante).get(NegocioTiposComprobante_.signo))));
  //            Predicate p = createWhereFromSearchFilter(sf, cb, root);
  //            p = appendAndPredicate(cb, cb.isNull(root.get(Comprobantes_.idRegistro)), p);
  //            cq.where(p);
  //            TypedQuery<BigDecimal> q = getEntityManager().createQuery(cq);
  //            return q.getSingleResult();
  //
  //        }
  //        return BigDecimal.ZERO;
  //    }
}
