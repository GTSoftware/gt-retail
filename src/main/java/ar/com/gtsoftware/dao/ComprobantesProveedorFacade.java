/*
 * Copyright 2018 GT Software.
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
import ar.com.gtsoftware.search.ComprobantesProveedorSearchFilter;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class ComprobantesProveedorFacade
    extends AbstractFacade<ProveedoresComprobantes, ComprobantesProveedorSearchFilter> {

  private final EntityManager em;

  public ComprobantesProveedorFacade(EntityManager em) {
    super(ProveedoresComprobantes.class);
    this.em = em;
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  @Override
  protected Predicate createWhereFromSearchFilter(
      ComprobantesProveedorSearchFilter vsf,
      CriteriaBuilder cb,
      Root<ProveedoresComprobantes> root) {

    Predicate p = null;
    if (vsf.getIdComprobante() != null) {
      p = cb.equal(root.get(ProveedoresComprobantes_.id), vsf.getIdComprobante());
    }
    if (vsf.getFechaComprobanteDesde() != null && vsf.getFechaComprobanteHasta() != null) {
      Predicate p1 =
          cb.between(
              root.get(ProveedoresComprobantes_.fechaComprobante),
              vsf.getFechaComprobanteDesde(),
              vsf.getFechaComprobanteHasta());
      p = appendAndPredicate(cb, p1, p);
    }
    if (vsf.getIdProveedor() != null) {
      Predicate p1 =
          cb.equal(
              root.get(ProveedoresComprobantes_.idProveedor).get(Personas_.id),
              vsf.getIdProveedor());
      p = appendAndPredicate(cb, p1, p);
    }
    if (vsf.getIdSucursal() != null) {
      Predicate p1 =
          cb.equal(
              root.get(ProveedoresComprobantes_.idSucursal).get(Sucursales_.id),
              vsf.getIdSucursal());
      p = appendAndPredicate(cb, p1, p);
    }

    if (vsf.getIdUsuario() != null) {
      Predicate p1 =
          cb.equal(
              root.get(ProveedoresComprobantes_.idUsuario).get(Usuarios_.id), vsf.getIdUsuario());
      p = appendAndPredicate(cb, p1, p);
    }
    if (vsf.getRegistradaEnLibroIVA() != null) {
      Predicate p1;
      if (vsf.getRegistradaEnLibroIVA()) {
        p1 = cb.isNotNull(root.get(ProveedoresComprobantes_.idRegistro));
      } else {
        p1 = cb.isNull(root.get(ProveedoresComprobantes_.idRegistro));
      }

      p = appendAndPredicate(cb, p1, p);
    }

    if (vsf.getAnulada() != null) {
      Predicate p1;
      if (vsf.getAnulada()) {
        p1 = cb.isTrue(root.get(ProveedoresComprobantes_.anulada));
      } else {
        p1 = cb.isFalse(root.get(ProveedoresComprobantes_.anulada));
      }

      p = appendAndPredicate(cb, p1, p);
    }

    if (StringUtils.isNotEmpty(vsf.getNumeroFactura())) {
      Predicate p1;
      Expression<String> nroFactura =
          cb.concat(
              root.get(ProveedoresComprobantes_.idRegistro)
                  .get(FiscalLibroIvaCompras_.letraFactura),
              root.get(ProveedoresComprobantes_.idRegistro)
                  .get(FiscalLibroIvaCompras_.puntoVentaFactura));
      nroFactura =
          cb.concat(
              nroFactura,
              root.get(ProveedoresComprobantes_.idRegistro)
                  .get(FiscalLibroIvaCompras_.numeroFactura));
      p1 = cb.like(nroFactura, String.format("%%%s%%", vsf.getNumeroFactura()));

      p = appendAndPredicate(cb, p1, p);
    }

    if (vsf.hasTiposComprobanteFilter()) {
      Predicate p1 =
          root.get(ProveedoresComprobantes_.tipoComprobante)
              .get(NegocioTiposComprobante_.id)
              .in(vsf.getIdTiposComprobanteList());
      p = appendAndPredicate(cb, p1, p);
    }

    return p;
  }

  /**
   * Obtiene el total de compras dado el filtro
   *
   * @param sf
   * @return
   */
  public BigDecimal calcularTotalComprasBySearchFilter(ComprobantesProveedorSearchFilter sf) {
    if (sf.hasFilter()) {
      CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
      CriteriaQuery<BigDecimal> cq = cb.createQuery(BigDecimal.class);
      Root<ProveedoresComprobantes> root = cq.from(ProveedoresComprobantes.class);
      cq.select(
          cb.sum(
              cb.prod(
                  root.get(ProveedoresComprobantes_.total),
                  root.get(ProveedoresComprobantes_.tipoComprobante)
                      .get(NegocioTiposComprobante_.signo))));
      Predicate p = createWhereFromSearchFilter(sf, cb, root);
      cq.where(p);
      TypedQuery<BigDecimal> q = getEntityManager().createQuery(cq);
      return q.getSingleResult();
    }
    return BigDecimal.ZERO;
  }

  /**
   * Obtiene el total de compras registradas en el libro de iva
   *
   * @param sf
   * @return
   */
  public BigDecimal calcularTotalComprasRegistradasBySearchFilter(
      ComprobantesProveedorSearchFilter sf) {
    if (sf.hasFilter()) {
      CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
      CriteriaQuery<BigDecimal> cq = cb.createQuery(BigDecimal.class);
      Root<ProveedoresComprobantes> root = cq.from(ProveedoresComprobantes.class);
      cq.select(
          cb.sum(
              cb.prod(
                  root.get(ProveedoresComprobantes_.total),
                  root.get(ProveedoresComprobantes_.tipoComprobante)
                      .get(NegocioTiposComprobante_.signo))));
      Predicate p = createWhereFromSearchFilter(sf, cb, root);
      p =
          appendAndPredicate(
              cb, cb.not(cb.isNull(root.get(ProveedoresComprobantes_.idRegistro))), p);
      cq.where(p);
      TypedQuery<BigDecimal> q = getEntityManager().createQuery(cq);
      return q.getSingleResult();
    }
    return BigDecimal.ZERO;
  }

  /**
   * Obtiene el total de compras sin registrar en el libro de iva
   *
   * @param sf
   * @return
   */
  public BigDecimal calcularTotalComprasSinRegistrarBySearchFilter(
      ComprobantesProveedorSearchFilter sf) {
    if (sf.hasFilter()) {
      CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
      CriteriaQuery<BigDecimal> cq = cb.createQuery(BigDecimal.class);
      Root<ProveedoresComprobantes> root = cq.from(ProveedoresComprobantes.class);
      cq.select(
          cb.sum(
              cb.prod(
                  root.get(ProveedoresComprobantes_.total),
                  root.get(ProveedoresComprobantes_.tipoComprobante)
                      .get(NegocioTiposComprobante_.signo))));
      Predicate p = createWhereFromSearchFilter(sf, cb, root);
      p = appendAndPredicate(cb, cb.isNull(root.get(ProveedoresComprobantes_.idRegistro)), p);
      cq.where(p);
      TypedQuery<BigDecimal> q = getEntityManager().createQuery(cq);
      return q.getSingleResult();
    }
    return BigDecimal.ZERO;
  }
}
