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
import ar.com.gtsoftware.search.CuponesSearchFilter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import java.time.LocalDateTime;
import org.springframework.stereotype.Repository;

@Repository
public class CuponesFacade extends AbstractFacade<Cupones, CuponesSearchFilter> {

  private final EntityManager em;

  public CuponesFacade(EntityManager em) {
    super(Cupones.class);
    this.em = em;
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  @Override
  public Predicate createWhereFromSearchFilter(
      CuponesSearchFilter sf, CriteriaBuilder cb, Root<Cupones> root) {

    Predicate p = null;
    if (sf.getIdCaja() != null) {
      Subquery<Long> subquery = cb.createQuery().subquery(Long.class);
      Root<Cupones> subRoot = subquery.from(Cupones.class);
      subquery.select(subRoot.get(Cupones_.id));

      Join<Cupones, RecibosDetalle> joinCup = subRoot.join(Cupones_.reciboDetalle);
      Join<RecibosDetalle, Recibos> joinRec = joinCup.join(RecibosDetalle_.idRecibo);

      subquery.where(cb.equal(joinRec.get(Recibos_.idCaja).get(Cajas_.id), sf.getIdCaja()));

      Predicate p1 = cb.in(root.get(Cupones_.id)).value(subquery);

      p = appendAndPredicate(cb, p, p1);
    }
    if (sf.hasValidFechasOrigen()) {
      Predicate p1 =
          cb.between(
              root.get(Cupones_.fechaOrigen), sf.getFechaOrigenDesde(), sf.getFechaOrigenHasta());
      p = appendAndPredicate(cb, p, p1);
    }
    return p;
  }

  /**
   * Establece la fecha de presentación de los cupones que pertenecen a la caja pasada como
   * parámetro y que aún no tienen fecha de presentación.
   *
   * @param idCaja la caja a la que pertenecen los cupones
   * @param fechaPresentacion la fecha de presentación a establecer
   */
  public void establecerFechaPresentacion(Cajas idCaja, LocalDateTime fechaPresentacion) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaUpdate<Cupones> update = cb.createCriteriaUpdate(Cupones.class);
    Root<Cupones> root = update.from(Cupones.class);
    update.set(Cupones_.fechaPresentacion, fechaPresentacion);

    Subquery<Long> subquery = update.subquery(Long.class);
    Root<Cupones> subRoot = subquery.from(Cupones.class);
    subquery.select(subRoot.get(Cupones_.id));

    Join<Cupones, RecibosDetalle> joinCup = subRoot.join(Cupones_.reciboDetalle);
    Join<RecibosDetalle, Recibos> joinRec = joinCup.join(RecibosDetalle_.idRecibo);

    subquery.where(cb.equal(joinRec.get(Recibos_.idCaja).get(Cajas_.id), idCaja.getId()));

    Predicate p = cb.in(root.get(Cupones_.id)).value(subquery);

    Predicate pNoPres = cb.isNull(root.get(Cupones_.fechaPresentacion));
    p = appendAndPredicate(cb, p, pNoPres);
    update.where(p);
    em.createQuery(update).executeUpdate();
  }
}
