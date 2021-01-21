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

import ar.com.gtsoftware.domain.ProductosRubros;
import ar.com.gtsoftware.domain.ProductosRubros_;
import ar.com.gtsoftware.search.RubrosSearchFilter;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

@Repository
public class ProductosRubrosFacade extends AbstractFacade<ProductosRubros, RubrosSearchFilter> {

  private final EntityManager em;

  public ProductosRubrosFacade(EntityManager em) {
    super(ProductosRubros.class);
    this.em = em;
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  @Override
  public Predicate createWhereFromSearchFilter(
      RubrosSearchFilter psf, CriteriaBuilder cb, Root<ProductosRubros> root) {

    Predicate p = null;
    if (psf.getNombreRubro() != null) {
      String s = psf.getNombreRubro().toUpperCase();
      p = cb.like(root.get(ProductosRubros_.nombreRubro), String.format("%%%s%%", s));
    }
    return p;
  }
}
