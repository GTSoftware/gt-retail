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

import ar.com.gtsoftware.domain.CajasMovimientos;
import ar.com.gtsoftware.domain.CajasMovimientos_;
import ar.com.gtsoftware.domain.Cajas_;
import ar.com.gtsoftware.search.CajasMovimientosSearchFilter;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

/** @author Rodrigo Tato <rotatomel@gmail.com> */
@Repository
public class CajasMovimientosFacade
    extends AbstractFacade<CajasMovimientos, CajasMovimientosSearchFilter> {

  private final EntityManager em;

  public CajasMovimientosFacade(EntityManager em) {
    super(CajasMovimientos.class);
    this.em = em;
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  @Override
  public Predicate createWhereFromSearchFilter(
      CajasMovimientosSearchFilter psf, CriteriaBuilder cb, Root<CajasMovimientos> root) {
    Predicate p = null;

    if (psf.getIdCaja() != null) {
      Predicate p1 = cb.equal(root.get(CajasMovimientos_.idCaja).get(Cajas_.id), psf.getIdCaja());
      p = appendAndPredicate(cb, p1, p);
    }

    if (psf.hasFechasFilter()) {
      Predicate p1 =
          cb.between(
              root.get(CajasMovimientos_.fechaMovimiento),
              psf.getFechaDesde(),
              psf.getFechaHasta());
      p = appendAndPredicate(cb, p1, p);
    }

    if (StringUtils.isNotEmpty(psf.getTxt())) {
      Predicate p1 =
          cb.like(
              root.get(CajasMovimientos_.descripcion),
              String.format("%%%s%%", psf.getTxt().toUpperCase()));
      p = appendAndPredicate(cb, p1, p);
    }

    return p;
  }
}
