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

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import ar.com.gtsoftware.domain.Depositos;
import ar.com.gtsoftware.domain.Depositos_;
import ar.com.gtsoftware.domain.Sucursales_;
import ar.com.gtsoftware.search.DepositosSearchFilter;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

@Repository
public class DepositosFacade extends AbstractFacade<Depositos, DepositosSearchFilter> {

  private final EntityManager em;

  public DepositosFacade(EntityManager em) {
    super(Depositos.class);
    this.em = em;
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  @Override
  public Predicate createWhereFromSearchFilter(
      DepositosSearchFilter sf, CriteriaBuilder cb, Root<Depositos> root) {
    Predicate p = null;

    if (sf.getActivo() != null) {
      Predicate p1 = cb.equal(root.get(Depositos_.activo), sf.getActivo());
      p = appendAndPredicate(cb, p, p1);
    }

    if (sf.getIdSucursal() != null) {
      Predicate p1 =
          cb.equal(root.get(Depositos_.idSucursal).get(Sucursales_.id), sf.getIdSucursal());
      p = appendAndPredicate(cb, p, p1);
    }
    if (isNotEmpty(sf.getNombreDeposito())) {
      throw new UnsupportedOperationException(
          "La busqueda por nombre de deposito no està implementada aun");
    }
    return p;
  }
}
