/*
 * Copyright 2016 GT Software.
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

import ar.com.gtsoftware.domain.*;
import ar.com.gtsoftware.search.RecibosSearchFilter;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

@Repository
public class RecibosFacade extends AbstractFacade<Recibos, RecibosSearchFilter> {

  private final EntityManager em;

  public RecibosFacade(EntityManager em) {
    super(Recibos.class);
    this.em = em;
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  @Override
  public Predicate createWhereFromSearchFilter(
      RecibosSearchFilter rsf, CriteriaBuilder cb, Root<Recibos> root) {
    Predicate p = null;

    if (rsf.getIdRecibo() != null) {
      Predicate p1 = cb.equal(root.get(Recibos_.id), rsf.getIdRecibo());
      p = appendAndPredicate(cb, p1, p);
    }

    if (rsf.getFechaDesde() != null && rsf.getFechaHasta() != null) {
      Predicate p1 =
          cb.between(root.get(Recibos_.fechaRecibo), rsf.getFechaDesde(), rsf.getFechaHasta());
      p = appendAndPredicate(cb, p1, p);
    }

    if (rsf.getIdCaja() != null) {
      Predicate p1 = cb.equal(root.get(Recibos_.idCaja).get(Cajas_.id), rsf.getIdCaja());
      p = appendAndPredicate(cb, p1, p);
    }

    if (rsf.getIdPersona() != null) {
      Predicate p1 = cb.equal(root.get(Recibos_.idPersona).get(Personas_.id), rsf.getIdPersona());
      p = appendAndPredicate(cb, p1, p);
    }

    if (rsf.getIdUsuario() != null) {
      Predicate p1 = cb.equal(root.get(Recibos_.idUsuario).get(Usuarios_.id), rsf.getIdUsuario());
      p = appendAndPredicate(cb, p1, p);
    }

    if (rsf.getIdSucursal() != null) {
      Predicate p1 =
          cb.equal(
              root.get(Recibos_.idCaja).get(Cajas_.idSucursal).get(Sucursales_.id),
              rsf.getIdSucursal());
      p = appendAndPredicate(cb, p1, p);
    }
    return p;
  }
}
