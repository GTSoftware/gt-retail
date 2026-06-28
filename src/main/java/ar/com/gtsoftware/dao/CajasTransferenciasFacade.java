/*
 * Copyright 2018 GT Software.
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
package ar.com.gtsoftware.dao;

import ar.com.gtsoftware.entity.*;
import ar.com.gtsoftware.search.CajasSearchFilter;
import ar.com.gtsoftware.search.CajasTransferenciasSearchFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import org.springframework.stereotype.Repository;

@Repository
public class CajasTransferenciasFacade
    extends AbstractFacade<CajasTransferencias, CajasTransferenciasSearchFilter> {

  private final EntityManager em;

  public CajasTransferenciasFacade(EntityManager em) {
    super(CajasTransferencias.class);
    this.em = em;
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  @Override
  public Predicate createWhereFromSearchFilter(
      CajasTransferenciasSearchFilter psf, CriteriaBuilder cb, Root<CajasTransferencias> root) {
    Predicate p = null;
    if (psf.hasFechasFilter()) {
      Predicate p1 =
          cb.between(
              root.get(CajasTransferencias_.fechaTransferencia),
              psf.getFechaDesde(),
              psf.getFechaHasta());
      p = appendAndPredicate(cb, p1, p);
    }

    if (psf.getIdCajaOrigen() != null) {
      Predicate p1 =
          cb.equal(
              root.get(CajasTransferencias_.idCajaOrigen).get(Cajas_.id), psf.getIdCajaOrigen());
      p = appendAndPredicate(cb, p1, p);
    }
    if (psf.getIdCajaDestino() != null) {
      Predicate p1 =
          cb.equal(
              root.get(CajasTransferencias_.idCajaDestino).get(Cajas_.id), psf.getIdCajaDestino());
      p = appendAndPredicate(cb, p1, p);
    }
    if (psf.getIdFormaPago() != null) {
      Predicate p1 =
          cb.equal(
              root.get(CajasTransferencias_.idFormaPago).get(NegocioFormasPago_.id),
              psf.getIdFormaPago());
      p = appendAndPredicate(cb, p1, p);
    }
    if (psf.getIdCaja() != null) {
      Predicate p1 =
          cb.equal(root.get(CajasTransferencias_.idCajaOrigen).get(Cajas_.id), psf.getIdCaja());
      Predicate p2 =
          cb.equal(root.get(CajasTransferencias_.idCajaDestino).get(Cajas_.id), psf.getIdCaja());
      p = appendAndPredicate(cb, cb.or(p1, p2), p);
    }
    return p;
  }

  public BigDecimal obtenerTotalTransferenciasEmitidas(@NotNull CajasSearchFilter csf) {
    if (csf.getIdCaja() == null) {
      throw new IllegalArgumentException("El idCaja no debe ser nulo");
    }

    CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
    CriteriaQuery<BigDecimal> cq = cb.createQuery(BigDecimal.class);
    Root<CajasTransferencias> root = cq.from(CajasTransferencias.class);

    Expression<BigDecimal> monto =
        cb.coalesce(root.get(CajasTransferencias_.monto), BigDecimal.ZERO);
    Expression<BigDecimal> total = cb.coalesce(cb.sum(monto), BigDecimal.ZERO);
    cq.select(total);

    Predicate p =
        cb.equal(root.get(CajasTransferencias_.idCajaOrigen).get(Cajas_.id), csf.getIdCaja());
    Predicate p1 = null;
    if (csf.getIdFormaPago() != null) {
      p1 =
          cb.equal(
              root.get(CajasTransferencias_.idFormaPago).get(NegocioFormasPago_.id),
              csf.getIdFormaPago());
    }

    p = appendAndPredicate(cb, p, p1);

    cq.where(p);
    TypedQuery<BigDecimal> q = getEntityManager().createQuery(cq);

    return q.getSingleResult();
  }

  public BigDecimal obtenerTotalTransferenciasRecibidas(@NotNull CajasSearchFilter csf) {
    if (csf.getIdCaja() == null) {
      throw new IllegalArgumentException("El idCaja no debe ser nulo");
    }

    CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
    CriteriaQuery<BigDecimal> cq = cb.createQuery(BigDecimal.class);
    Root<CajasTransferencias> root = cq.from(CajasTransferencias.class);

    Expression<BigDecimal> monto =
        cb.coalesce(root.get(CajasTransferencias_.monto), BigDecimal.ZERO);
    Expression<BigDecimal> total = cb.coalesce(cb.sum(monto), BigDecimal.ZERO);
    cq.select(total);

    Predicate p =
        cb.equal(root.get(CajasTransferencias_.idCajaDestino).get(Cajas_.id), csf.getIdCaja());
    Predicate p1 = null;
    if (csf.getIdFormaPago() != null) {
      p1 =
          cb.equal(
              root.get(CajasTransferencias_.idFormaPago).get(NegocioFormasPago_.id),
              csf.getIdFormaPago());
    }

    p = appendAndPredicate(cb, p, p1);

    cq.where(p);
    TypedQuery<BigDecimal> q = getEntityManager().createQuery(cq);

    return q.getSingleResult();
  }
}
