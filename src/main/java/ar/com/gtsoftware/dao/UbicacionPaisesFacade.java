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

import ar.com.gtsoftware.domain.UbicacionPaises;
import ar.com.gtsoftware.domain.UbicacionPaises_;
import ar.com.gtsoftware.search.PaisesSearchFilter;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Repository
public class UbicacionPaisesFacade extends AbstractFacade<UbicacionPaises, PaisesSearchFilter> {


    private final EntityManager em;

    public UbicacionPaisesFacade(EntityManager em) {
        super(UbicacionPaises.class);
        this.em = em;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public Predicate createWhereFromSearchFilter(PaisesSearchFilter psf, CriteriaBuilder cb, Root<UbicacionPaises> root) {
        Predicate p = null;
        if (psf.getIdPais() != null) {
            p = cb.equal(root.get(UbicacionPaises_.id), psf.getIdPais());
        }
        if (psf.getNombrePais() != null) {
            Predicate p1 = cb.like(root.get(UbicacionPaises_.nombrePais), String.format("%%%s%%", psf.getNombrePais().toUpperCase()));
            p = appendOrPredicate(cb, p, p1);
        }
        return p;
    }

}
