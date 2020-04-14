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

import ar.com.gtsoftware.domain.LegalGeneros;
import ar.com.gtsoftware.domain.LegalGeneros_;
import ar.com.gtsoftware.domain.LegalTiposPersoneria_;
import ar.com.gtsoftware.search.GenerosSearchFilter;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Repository
public class LegalGenerosFacade extends AbstractFacade<LegalGeneros, GenerosSearchFilter> {


    private final EntityManager em;

    public LegalGenerosFacade(EntityManager em) {
        super(LegalGeneros.class);
        this.em = em;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public Predicate createWhereFromSearchFilter(GenerosSearchFilter gsf, CriteriaBuilder cb, Root<LegalGeneros> root) {
        Predicate p = null;
        if (gsf.getIdGenero() != null) {
            p = cb.equal(root.get(LegalGeneros_.id), gsf.getIdGenero());
        }
        if (gsf.getIdTipoPersoneria() != null) {
            Predicate p1 = cb.equal(root.get(LegalGeneros_.idTipoPersoneria).get(LegalTiposPersoneria_.id), gsf.getIdTipoPersoneria());
            p = appendAndPredicate(cb, p, p1);
        }
        return p;
    }

}
