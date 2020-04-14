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

import ar.com.gtsoftware.domain.NegocioTiposComprobante;
import ar.com.gtsoftware.domain.NegocioTiposComprobante_;
import ar.com.gtsoftware.search.NegocioTiposComprobanteSearchFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Repository
public class NegocioTiposComprobanteFacade extends AbstractFacade<NegocioTiposComprobante, NegocioTiposComprobanteSearchFilter> {


    private final EntityManager em;

    public NegocioTiposComprobanteFacade(EntityManager em) {
        super(NegocioTiposComprobante.class);
        this.em = em;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public Predicate createWhereFromSearchFilter(NegocioTiposComprobanteSearchFilter nsf, CriteriaBuilder cb, Root<NegocioTiposComprobante> root) {

        Predicate p = null;
        if (nsf.getActivo() != null) {
            Predicate p1 = cb.equal(root.get(NegocioTiposComprobante_.activo), nsf.getActivo());
            p = appendAndPredicate(cb, p, p1);
        }
        if (StringUtils.isNotEmpty(nsf.getNombre())) {
            String s = nsf.getNombre().toUpperCase();
            Predicate p1 = cb.like(root.get(NegocioTiposComprobante_.nombreComprobante), String.format("%%%s%%", s));
            p = appendAndPredicate(cb, p, p1);
        }
        return p;

    }

}
