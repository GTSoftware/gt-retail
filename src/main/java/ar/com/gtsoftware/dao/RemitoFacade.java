/*
 * Copyright 2017 GT Software.
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

import ar.com.gtsoftware.domain.*;
import ar.com.gtsoftware.search.RemitoSearchFilter;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

@Repository
public class RemitoFacade extends AbstractFacade<Remito, RemitoSearchFilter> {


    private final EntityManager em;

    public RemitoFacade(EntityManager em) {
        super(Remito.class);
        this.em = em;
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }

    @Override
    protected Predicate createWhereFromSearchFilter(RemitoSearchFilter sf, CriteriaBuilder cb, Root<Remito> root) {
        Predicate p = null;
        if (sf.hasEntreFechasAltaFilter()) {
            Predicate p1 = cb.between(root.get(Remito_.fechaAlta), sf.getFechaAltaDesde(), sf.getFechaAltaHasta());
            p = appendAndPredicate(cb, p1, p);
        }

        if (sf.getIdTipoMovimiento() != null) {
            Predicate p1 = cb.equal(root.get(Remito_.remitoTipoMovimiento).get(RemitoTipoMovimiento_.id),
                    sf.getIdTipoMovimiento());
            p = appendAndPredicate(cb, p1, p);
        }

        if (sf.getIdProducto() != null) {
            Subquery<Long> subQRemDetProd = cb.createQuery().subquery(Long.class);
            Root<RemitoDetalle> fromSubQ = subQRemDetProd.from(RemitoDetalle.class);

            subQRemDetProd.select(fromSubQ.get(RemitoDetalle_.id));

            Predicate ps1 = cb.equal(fromSubQ.get(RemitoDetalle_.idProducto).get(Productos_.id), sf.getIdProducto());
            Predicate ps2 = cb.equal(fromSubQ.get(RemitoDetalle_.remitoCabecera), root);
            subQRemDetProd.where(cb.and(ps1, ps2));

            Predicate p1 = cb.exists(subQRemDetProd);

            p = appendAndPredicate(cb, p1, p);
        }
        return p;
    }

}
