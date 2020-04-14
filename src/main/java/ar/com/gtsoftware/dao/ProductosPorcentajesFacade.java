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

import ar.com.gtsoftware.domain.ProductosPorcentajes;
import ar.com.gtsoftware.domain.ProductosPorcentajes_;
import ar.com.gtsoftware.domain.ProductosTiposPorcentajes_;
import ar.com.gtsoftware.domain.Productos_;
import ar.com.gtsoftware.search.ProductosPorcentajesSearchFilter;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Repository
public class ProductosPorcentajesFacade extends AbstractFacade<ProductosPorcentajes, ProductosPorcentajesSearchFilter> {

    private final EntityManager em;

    public ProductosPorcentajesFacade(EntityManager em) {
        super(ProductosPorcentajes.class);
        this.em = em;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public Predicate createWhereFromSearchFilter(ProductosPorcentajesSearchFilter psf, CriteriaBuilder cb, Root<ProductosPorcentajes> root) {

        Predicate p = null;
        if (psf.getIdProducto() != null) {
            Predicate p1 = cb.equal(root.get(ProductosPorcentajes_.idProducto).get(Productos_.id), psf.getIdProducto());
            p = appendAndPredicate(cb, p, p1);
        }
        if (psf.getIdTipoPorcentaje() != null) {
            Predicate p1 = cb.equal(root.get(ProductosPorcentajes_.idTipoPorcentaje).get(ProductosTiposPorcentajes_.id), psf.getIdTipoPorcentaje());
            p = appendAndPredicate(cb, p, p1);

        }
        return p;
    }

}
