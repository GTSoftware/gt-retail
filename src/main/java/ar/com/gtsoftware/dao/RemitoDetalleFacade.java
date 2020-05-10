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

import ar.com.gtsoftware.domain.RemitoDetalle;
import ar.com.gtsoftware.domain.RemitoDetalle_;
import ar.com.gtsoftware.domain.Remito_;
import ar.com.gtsoftware.search.RemitoDetalleSearchFilter;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Repository
public class RemitoDetalleFacade extends AbstractFacade<RemitoDetalle, RemitoDetalleSearchFilter> {


    private final EntityManager em;

    public RemitoDetalleFacade(EntityManager em) {
        super(RemitoDetalle.class);
        this.em = em;
    }

    @Override
    protected EntityManager getEntityManager() {
        return this.em;
    }

    @Override
    protected Predicate createWhereFromSearchFilter(RemitoDetalleSearchFilter sf, CriteriaBuilder cb, Root<RemitoDetalle> root) {
        Predicate p = null;

        if (sf.hasEntreFechasAltaFilter()) {
            Predicate p1 = cb.between(root.get(RemitoDetalle_.remitoCabecera).get(Remito_.fechaAlta), sf.getFechaRemitoDesde(), sf.getFechaRemitoHasta());
            p = appendAndPredicate(cb, p1, p);
        }

        if (sf.getIdDepositoMovimiento() != null) {
            Predicate p1 = cb.equal(root.get(RemitoDetalle_.remitoCabecera).get(Remito_.idOrigenInterno), sf.getIdDepositoMovimiento());
            Predicate p2 = cb.equal(root.get(RemitoDetalle_.remitoCabecera).get(Remito_.idDestinoPrevistoInterno), sf.getIdDepositoMovimiento());
            Predicate p3 = cb.or(p1, p2);

            p = appendAndPredicate(cb, p3, p);
        }

        if (sf.getIdProducto() != null) {
            Predicate p1 = cb.equal(root.get(RemitoDetalle_.idProducto), sf.getIdProducto());

            p = appendAndPredicate(cb, p1, p);
        }
        return p;
    }

}
