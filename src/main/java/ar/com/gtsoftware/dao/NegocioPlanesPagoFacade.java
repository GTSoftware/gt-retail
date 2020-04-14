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

import ar.com.gtsoftware.domain.NegocioFormasPago_;
import ar.com.gtsoftware.domain.NegocioPlanesPago;
import ar.com.gtsoftware.domain.NegocioPlanesPago_;
import ar.com.gtsoftware.search.PlanesPagoSearchFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Repository
public class NegocioPlanesPagoFacade extends AbstractFacade<NegocioPlanesPago, PlanesPagoSearchFilter> {


    private final EntityManager em;

    public NegocioPlanesPagoFacade(EntityManager em) {
        super(NegocioPlanesPago.class);
        this.em = em;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public Predicate createWhereFromSearchFilter(PlanesPagoSearchFilter sf, CriteriaBuilder cb, Root<NegocioPlanesPago> root) {
        Predicate p = null;

        if (StringUtils.isNotEmpty(sf.getNombre())) {
            Predicate p1 = cb.like(cb.upper(root.get(NegocioPlanesPago_.nombre)), String.format("%%%s%%", sf.getNombre().toUpperCase()));
            p = appendAndPredicate(cb, p, p1);
        }

        if (sf.getIdFormaPago() != null) {
            Predicate p1 = cb.equal(root.get(NegocioPlanesPago_.idFormaPago).get(NegocioFormasPago_.id), sf.getIdFormaPago());
            p = appendAndPredicate(cb, p, p1);
        }

        if (sf.getActivo() != null) {
            Predicate p1 = cb.between(cb.currentTimestamp(), root.get(NegocioPlanesPago_.fechaActivoDesde),
                    root.get(NegocioPlanesPago_.fechaActivoHasta));
            if (!sf.getActivo()) {
                p1 = cb.not(p1);
            }
            p = appendAndPredicate(cb, p, p1);
        }
        return p;

    }

}
