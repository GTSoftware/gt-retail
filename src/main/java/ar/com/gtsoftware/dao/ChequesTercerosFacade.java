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

import ar.com.gtsoftware.domain.ChequesTerceros;
import ar.com.gtsoftware.domain.ChequesTerceros_;
import ar.com.gtsoftware.search.ChequesTercerosSearchFilter;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


@Repository
public class ChequesTercerosFacade extends AbstractFacade<ChequesTerceros, ChequesTercerosSearchFilter> {


    private final EntityManager em;

    public ChequesTercerosFacade(EntityManager em) {
        super(ChequesTerceros.class);
        this.em = em;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public Predicate createWhereFromSearchFilter(ChequesTercerosSearchFilter sf, CriteriaBuilder cb, Root<ChequesTerceros> root) {

        Predicate p = null;
        if (sf.hasFilterBanco()) {
            Predicate p1 = cb.equal(root.get(ChequesTerceros_.idBanco), sf.getIdBanco());
            p = appendAndPredicate(cb, p1, p);
        }
        if (sf.hasFilterCuitOriginante()) {
            Predicate p1 = cb.equal(root.get(ChequesTerceros_.cuitOriginante), sf.getCuitOriginante());
            p = appendAndPredicate(cb, p1, p);
        }
        if (sf.hasFilterFechaOrigen()) {
            Predicate p1 = cb.between(root.get(ChequesTerceros_.fechaOrigen), sf.getFechaOrigenDesde(),
                    sf.getFechaOrigenHasta());
            p = appendAndPredicate(cb, p1, p);
        }
        if (sf.hasFilterNoCobrados()) {
            Predicate p1;
            if (sf.getNoCobrados()) {
                p1 = cb.isNull(root.get(ChequesTerceros_.fechaCobro));
            } else {
                p1 = cb.isNotNull(root.get(ChequesTerceros_.fechaCobro));
            }
            p = appendAndPredicate(cb, p1, p);
        }
        if (sf.hasFilterNoVencidos()) {
            Predicate p1;
            if (sf.getNoVencidos()) {
                p1 = cb.greaterThanOrEqualTo(root.get(ChequesTerceros_.fechaVencimiento), cb.currentDate());
            } else {
                p1 = cb.lessThanOrEqualTo(root.get(ChequesTerceros_.fechaVencimiento), cb.currentDate());
            }
            p = appendAndPredicate(cb, p1, p);
        }
        if (sf.hasFilterNombreOriginante()) {
            String s = sf.getNombreOriginante().toUpperCase();
            Predicate p1 = cb.like(root.get(ChequesTerceros_.razonSocialOriginante), String.format("%%%s%%", s));
            p = appendAndPredicate(cb, p1, p);
        }
        return p;
    }

}
