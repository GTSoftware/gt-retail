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

import ar.com.gtsoftware.domain.ProductosRubros_;
import ar.com.gtsoftware.domain.ProductosSubRubros;
import ar.com.gtsoftware.domain.ProductosSubRubros_;
import ar.com.gtsoftware.search.SubRubroSearchFilter;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


@Repository
public class ProductosSubRubrosFacade extends AbstractFacade<ProductosSubRubros, SubRubroSearchFilter> {

    private final EntityManager em;

    public ProductosSubRubrosFacade(EntityManager em) {
        super(ProductosSubRubros.class);
        this.em = em;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public Predicate createWhereFromSearchFilter(SubRubroSearchFilter psf, CriteriaBuilder cb, Root<ProductosSubRubros> root) {

        Predicate p = null;
        if (psf.getNombreSubRubro() != null) {
            String s = psf.getNombreSubRubro().toUpperCase();
            p = cb.like(root.get(ProductosSubRubros_.nombreSubRubro), String.format("%%%s%%", s));
        }
        if (psf.getIdProductosRubros() != null) {
            Predicate p1 = cb.equal(root.get(ProductosSubRubros_.idRubro).get(ProductosRubros_.id), psf.getIdProductosRubros());
            p = appendAndPredicate(cb, p1, p);
        }

        return p;
    }

}
