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

import ar.com.gtsoftware.domain.FiscalTiposComprobante;
import ar.com.gtsoftware.domain.FiscalTiposComprobante_;
import ar.com.gtsoftware.domain.NegocioTiposComprobante_;
import ar.com.gtsoftware.search.FiscalTiposComprobanteSearchFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Repository
public class FiscalTiposComprobanteFacade extends AbstractFacade<FiscalTiposComprobante, FiscalTiposComprobanteSearchFilter> {


    private final EntityManager em;

    public FiscalTiposComprobanteFacade(EntityManager em) {
        super(FiscalTiposComprobante.class);
        this.em = em;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public Predicate createWhereFromSearchFilter(FiscalTiposComprobanteSearchFilter ftsf, CriteriaBuilder cb, Root<FiscalTiposComprobante> root) {

        Predicate p = null;
        if (StringUtils.isNotEmpty(ftsf.getLetra())) {
            Predicate p1 = cb.equal(root.get(FiscalTiposComprobante_.letra), ftsf.getLetra());
            p = appendAndPredicate(cb, p, p1);
        }
        if (ftsf.getIdTipoComprobante() != null) {
            Predicate p1 = cb.equal(root.get(FiscalTiposComprobante_.tipoComprobante).get(NegocioTiposComprobante_.id), ftsf.getIdTipoComprobante());
            p = appendAndPredicate(cb, p, p1);
        }

        return p;
    }

}
