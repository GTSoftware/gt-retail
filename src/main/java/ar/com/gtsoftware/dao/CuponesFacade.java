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
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
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
      Join<Cupones, RecibosDetalle> joinCup = root.join(Cupones_.reciboDetalle);
      Join<RecibosDetalle, Recibos> joinRec = joinCup.join(RecibosDetalle_.idRecibo);
      Predicate p1 = cb.equal(joinRec.get(Recibos_.idCaja).get(Cajas_.id), sf.getIdCaja());
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

  public void establecerFechaPresentacion(Cajas idCaja, Date fechaPresentacion) {
    CriteriaBuilder cb = this.em.getCriteriaBuilder();
    CriteriaUpdate<Cupones> update = cb.createCriteriaUpdate(Cupones.class);
    Root root = update.from(Cupones.class);
    update.set(Cupones_.fechaPresentacion, fechaPresentacion);
    CuponesSearchFilter sf = CuponesSearchFilter.builder().idCaja(idCaja.getId()).build();
    Predicate p = createWhereFromSearchFilter(sf, cb, root);
    Predicate pNoPres = cb.isNull(root.get(Cupones_.fechaPresentacion));
    p = appendAndPredicate(cb, p, pNoPres);
    update.where(p);
    this.em.createQuery(update).executeUpdate();
  }
}
