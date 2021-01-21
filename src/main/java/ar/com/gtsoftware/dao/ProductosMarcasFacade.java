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

import ar.com.gtsoftware.domain.ProductosMarcas;
import ar.com.gtsoftware.domain.ProductosMarcas_;
import ar.com.gtsoftware.search.MarcasSearchFilter;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

@Repository
public class ProductosMarcasFacade extends AbstractFacade<ProductosMarcas, MarcasSearchFilter> {

  private final EntityManager em;

  public ProductosMarcasFacade(EntityManager em) {
    super(ProductosMarcas.class);
    this.em = em;
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  @Override
  public Predicate createWhereFromSearchFilter(
      MarcasSearchFilter msf, CriteriaBuilder cb, Root<ProductosMarcas> root) {

    Predicate p = null;
    if (msf.getNombreMarca() != null) {
      String s = msf.getNombreMarca().toUpperCase();
      p = cb.like(root.get(ProductosMarcas_.nombreMarca), String.format("%%%s%%", s));
    }

    return p;
  }
}
