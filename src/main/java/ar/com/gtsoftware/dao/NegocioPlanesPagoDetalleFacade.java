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

import ar.com.gtsoftware.entity.NegocioPlanesPagoDetalle;
import ar.com.gtsoftware.entity.NegocioPlanesPagoDetalle_;
import ar.com.gtsoftware.entity.NegocioPlanesPago_;
import ar.com.gtsoftware.search.PlanesPagoDetalleSearchFilter;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

@Repository
public class NegocioPlanesPagoDetalleFacade
    extends AbstractFacade<NegocioPlanesPagoDetalle, PlanesPagoDetalleSearchFilter> {

  private final EntityManager em;

  public NegocioPlanesPagoDetalleFacade(EntityManager em) {
    super(NegocioPlanesPagoDetalle.class);
    this.em = em;
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  @Override
  public Predicate createWhereFromSearchFilter(
      PlanesPagoDetalleSearchFilter sf, CriteriaBuilder cb, Root<NegocioPlanesPagoDetalle> root) {
    Predicate p = null;

    if (sf.getIdPlan() != null) {
      Predicate p1 =
          cb.equal(
              root.get(NegocioPlanesPagoDetalle_.idPlan).get(NegocioPlanesPago_.id),
              sf.getIdPlan());
      p = appendAndPredicate(cb, p, p1);
    }

    if (sf.getActivo() != null) {
      Predicate p1 = cb.equal(root.get(NegocioPlanesPagoDetalle_.activo), sf.getActivo());
      p = appendAndPredicate(cb, p, p1);
    }

    return p;
  }
}
