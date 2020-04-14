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

import ar.com.gtsoftware.domain.*;
import ar.com.gtsoftware.search.CajasSearchFilter;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author Rodrigo Tato <rotatomel@gmail.com>
 */
@Repository
public class CajasFacade extends AbstractFacade<Cajas, CajasSearchFilter> {


    private final EntityManager em;

    public CajasFacade(EntityManager em) {
        super(Cajas.class);
        this.em = em;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public Predicate createWhereFromSearchFilter(CajasSearchFilter psf, CriteriaBuilder cb, Root<Cajas> root) {
        Predicate p = null;

        if (psf.getIdCaja() != null) {
            Predicate p1 = cb.equal(root.get(Cajas_.id), psf.getIdCaja());
            p = appendAndPredicate(cb, p, p1);
        }

        if (psf.getAbierta() != null) {
            Predicate p1 = cb.isNotNull(root.get(Cajas_.fechaCierre));
            if (psf.getAbierta()) {
                p1 = cb.isNull(root.get(Cajas_.fechaCierre));
            }
            p = appendAndPredicate(cb, p, p1);
        }
        if (psf.getIdSucursal() != null) {
            Predicate p1 = cb.equal(root.get(Cajas_.idSucursal).get(Sucursales_.id), psf.getIdSucursal());
            p = appendAndPredicate(cb, p, p1);
        }
        if (psf.getIdUsuario() != null) {
            Predicate p1 = cb.equal(root.get(Cajas_.idUsuario).get(Usuarios_.id), psf.getIdUsuario());
            p = appendAndPredicate(cb, p, p1);
        }
        return p;

    }

    public BigDecimal obtenerTotalDeCaja(@NotNull CajasSearchFilter csf) {
        if (csf.getIdCaja() == null) {
            throw new IllegalArgumentException("El idCaja no debe ser nulo");
        }

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<BigDecimal> cq = cb.createQuery(BigDecimal.class);
        Root<RecibosDetalle> root = cq.from(RecibosDetalle.class);
        Join<RecibosDetalle, Recibos> recibos = root.join(RecibosDetalle_.idRecibo, JoinType.INNER);
        CriteriaBuilder.Coalesce<BigDecimal> coalesce = cb.coalesce();
        coalesce.value(cb.sum(cb.sum(root.get(RecibosDetalle_.montoPagadoConSigno), root.get(RecibosDetalle_.redondeo))));
        coalesce.value(BigDecimal.ZERO);
        cq.select(coalesce);
        Predicate p = cb.equal(recibos.get(Recibos_.idCaja).get(Cajas_.id), csf.getIdCaja());
        Predicate p1 = null;
        if (csf.getIdFormaPago() != null) {
            p1 = cb.equal(root.get(RecibosDetalle_.idFormaPago).get(NegocioFormasPago_.id), csf.getIdFormaPago());
        }

        p = appendAndPredicate(cb, p, p1);

        cq.where(p);
        TypedQuery<BigDecimal> q = getEntityManager().createQuery(cq);
        return q.getSingleResult();

    }


}
