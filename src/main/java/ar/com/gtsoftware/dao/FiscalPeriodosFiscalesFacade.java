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

import ar.com.gtsoftware.domain.FiscalPeriodosFiscales;
import ar.com.gtsoftware.domain.FiscalPeriodosFiscales_;
import ar.com.gtsoftware.search.FiscalPeriodosFiscalesSearchFilter;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;

@Repository
public class FiscalPeriodosFiscalesFacade extends AbstractFacade<FiscalPeriodosFiscales, FiscalPeriodosFiscalesSearchFilter> {


    private final EntityManager em;

    public FiscalPeriodosFiscalesFacade(EntityManager em) {
        super(FiscalPeriodosFiscales.class);
        this.em = em;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public Predicate createWhereFromSearchFilter(FiscalPeriodosFiscalesSearchFilter psf, CriteriaBuilder cb, Root<FiscalPeriodosFiscales> root) {

        Predicate p = null;
        if (psf.getVigente() != null) {
            Predicate p3 = cb.between(cb.literal(LocalDateTime.now()),
                    root.get(FiscalPeriodosFiscales_.fechaInicioPeriodo),
                    root.get(FiscalPeriodosFiscales_.fechaFinPeriodo));
            if (psf.getVigente()) {
                p = appendAndPredicate(cb, p, p3);
            } else {
                p = appendAndPredicate(cb, p, cb.not(p3));
            }
        }

        if (psf.getCerrado() != null) {
            Predicate p1 = cb.equal(root.get(FiscalPeriodosFiscales_.periodoCerrado), psf.getCerrado());
            p = appendAndPredicate(cb, p, p1);
        }

        if (psf.getFechaActual() != null) {
            Predicate pDesde = cb.lessThanOrEqualTo(root.get(FiscalPeriodosFiscales_.fechaInicioPeriodo), psf.getFechaActual());
            Predicate pHasta = cb.greaterThanOrEqualTo(root.get(FiscalPeriodosFiscales_.fechaFinPeriodo), psf.getFechaActual());
            p = appendAndPredicate(cb, p, pDesde);
            p = appendAndPredicate(cb, p, pHasta);
        }
        return p;
    }

}
