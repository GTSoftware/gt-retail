/*
 * Copyright 2017 GT Software.
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
import ar.com.gtsoftware.search.ProductoXDepositoSearchFilter;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

@Repository
public class ProductoXDepositoFacade
    extends AbstractFacade<ProductoXDeposito, ProductoXDepositoSearchFilter> {

  private final EntityManager em;

  public ProductoXDepositoFacade(EntityManager em) {
    super(ProductoXDeposito.class);
    this.em = em;
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  @Override
  protected Predicate createWhereFromSearchFilter(
      ProductoXDepositoSearchFilter sf, CriteriaBuilder cb, Root<ProductoXDeposito> root) {

    Predicate p = null;

    if (sf.hasIdProducto()) {
      Predicate p1 =
          cb.equal(root.get(ProductoXDeposito_.producto).get(Productos_.id), sf.getIdProducto());
      p = appendAndPredicate(cb, p, p1);
    }

    if (sf.hasIdDeposito()) {
      Predicate p1 =
          cb.equal(root.get(ProductoXDeposito_.deposito).get(Depositos_.id), sf.getIdDeposito());
      p = appendAndPredicate(cb, p, p1);
    }

    if (sf.hasIdSucursal()) {
      Predicate p1 =
          cb.equal(
              root.get(ProductoXDeposito_.deposito).get(Depositos_.idSucursal).get(Sucursales_.id),
              sf.getIdSucursal());
      p = appendAndPredicate(cb, p, p1);
    }

    return p;
  }

  /**
   * Devuelve la sumatoria de stock según el filtro pasado por parámetro
   *
   * @param sf
   * @return la suma del stock
   */
  public BigDecimal getStockBySearchFilter(ProductoXDepositoSearchFilter sf) {
    if (sf == null) {
      return null;
    }
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<BigDecimal> cq = cb.createQuery(BigDecimal.class);
    Root<ProductoXDeposito> root = cq.from(ProductoXDeposito.class);
    CriteriaBuilder.Coalesce<BigDecimal> coalesce = cb.coalesce();
    coalesce.value(cb.sum(root.get(ProductoXDeposito_.stock)));
    coalesce.value(BigDecimal.ZERO);
    cq.select(coalesce.alias("CANT_STOCK"));
    if (sf.hasFilter()) {
      cq.where(createWhereFromSearchFilter(sf, cb, root));
    }
    BigDecimal result = em.createQuery(cq).getSingleResult();
    if (result == null) {
      return BigDecimal.ZERO;
    }
    return result;
  }
}
