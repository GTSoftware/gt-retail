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

import ar.com.gtsoftware.entity.ProductosListasPrecios_;
import ar.com.gtsoftware.entity.ProductosPrecios;
import ar.com.gtsoftware.entity.ProductosPrecios_;
import ar.com.gtsoftware.entity.Productos_;
import ar.com.gtsoftware.search.ProductosPreciosSearchFilter;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

@Repository
public class ProductosPreciosFacade
    extends AbstractFacade<ProductosPrecios, ProductosPreciosSearchFilter> {

  private final EntityManager em;

  public ProductosPreciosFacade(EntityManager em) {
    super(ProductosPrecios.class);
    this.em = em;
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  @Override
  public Predicate createWhereFromSearchFilter(
      ProductosPreciosSearchFilter psf, CriteriaBuilder cb, Root<ProductosPrecios> root) {

    Predicate p = null;

    if (psf.getIdProducto() != null) {
      Predicate p1 =
          cb.equal(root.get(ProductosPrecios_.idProducto).get(Productos_.id), psf.getIdProducto());
      p = appendAndPredicate(cb, p, p1);
    }

    if (psf.getIdListaPrecios() != null) {
      Predicate p1 =
          cb.equal(
              root.get(ProductosPrecios_.idListaPrecios).get(ProductosListasPrecios_.id),
              psf.getIdListaPrecios());
      p = appendAndPredicate(cb, p, p1);
    }
    return p;
  }
}
