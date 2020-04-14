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

import ar.com.gtsoftware.domain.ComprobantesPagos;
import ar.com.gtsoftware.domain.ComprobantesPagos_;
import ar.com.gtsoftware.domain.Comprobantes_;
import ar.com.gtsoftware.search.ComprobantesPagosSearchFilter;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Repository
public class ComprobantesPagosFacade extends AbstractFacade<ComprobantesPagos, ComprobantesPagosSearchFilter> {


    private final EntityManager em;

    public ComprobantesPagosFacade(EntityManager em) {
        super(ComprobantesPagos.class);
        this.em = em;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public Predicate createWhereFromSearchFilter(ComprobantesPagosSearchFilter sf, CriteriaBuilder cb, Root<ComprobantesPagos> root) {
        Predicate p = null;
        if (sf.getIdComprobante() != null) {
            Predicate p1 = cb.equal(root.get(ComprobantesPagos_.idComprobante).get(Comprobantes_.id), sf.getIdComprobante());
            p = appendAndPredicate(cb, p, p1);
        }

        if (sf.getConSaldo() != null) {
            Predicate p1 = cb.equal(root.get(ComprobantesPagos_.montoPago), root.get(ComprobantesPagos_.montoPagado));
            if (sf.getConSaldo()) {
                p1 = p1.not();
            }
            p = appendAndPredicate(cb, p, p1);
        }

        return p;

    }

}
