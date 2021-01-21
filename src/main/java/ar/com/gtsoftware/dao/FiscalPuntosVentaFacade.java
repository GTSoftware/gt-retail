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

import ar.com.gtsoftware.domain.FiscalPuntosVenta;
import ar.com.gtsoftware.domain.FiscalPuntosVenta_;
import ar.com.gtsoftware.domain.Sucursales_;
import ar.com.gtsoftware.search.FiscalPuntosVentaSearchFilter;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

@Repository
public class FiscalPuntosVentaFacade
    extends AbstractFacade<FiscalPuntosVenta, FiscalPuntosVentaSearchFilter> {

  private final EntityManager em;

  public FiscalPuntosVentaFacade(EntityManager em) {
    super(FiscalPuntosVenta.class);
    this.em = em;
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  @Override
  public Predicate createWhereFromSearchFilter(
      FiscalPuntosVentaSearchFilter pvsf, CriteriaBuilder cb, Root<FiscalPuntosVenta> root) {

    Predicate p = null;
    if (pvsf.getIdSucursal() != null) {
      Predicate p1 =
          cb.equal(root.get(FiscalPuntosVenta_.sucursal).get(Sucursales_.id), pvsf.getIdSucursal());
      p = appendAndPredicate(cb, p, p1);
    }

    if (pvsf.getActivo() != null) {
      Predicate p1 = cb.equal(root.get(FiscalPuntosVenta_.activo), pvsf.getActivo());
      p = appendAndPredicate(cb, p, p1);
    }

    if (pvsf.getNroPuntoVenta() != null) {
      Predicate p1 = cb.equal(root.get(FiscalPuntosVenta_.nroPuntoVenta), pvsf.getNroPuntoVenta());
      p = appendAndPredicate(cb, p, p1);
    }

    if (pvsf.hasTiposPuntoVentaFilter()) {
      Predicate p1 = root.get(FiscalPuntosVenta_.tipo).in(pvsf.getTiposPuntoVenta());
      p = appendAndPredicate(cb, p, p1);
    }
    return p;
  }
}
