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

import ar.com.gtsoftware.domain.ProductosListasPrecios;
import ar.com.gtsoftware.domain.ProductosListasPrecios_;
import ar.com.gtsoftware.search.ProductosListasPreciosSearchFilter;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Repository
public class ProductosListasPreciosFacade extends AbstractFacade<ProductosListasPrecios, ProductosListasPreciosSearchFilter> {


    private final EntityManager em;

    public ProductosListasPreciosFacade(EntityManager em) {
        super(ProductosListasPrecios.class);
        this.em = em;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    protected Predicate createWhereFromSearchFilter(ProductosListasPreciosSearchFilter psf, CriteriaBuilder cb, Root<ProductosListasPrecios> root) {

        Predicate p = null;

        if (psf.getNombre() != null && !psf.getNombre().isEmpty()) {
            String s = psf.getNombre().toUpperCase();
            Predicate p1 = cb.like(root.get(ProductosListasPrecios_.nombreLista), String.format("%%%s%%", s));

            p = appendAndPredicate(cb, p, p1);
        }

        if (psf.getActiva() != null) {
            Predicate p1 = cb.equal(root.get(ProductosListasPrecios_.activa), psf.getActiva());
            p = appendAndPredicate(cb, p, p1);
        }
        return p;
    }

}
