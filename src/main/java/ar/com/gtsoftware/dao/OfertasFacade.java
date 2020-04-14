/*
 * Copyright 2018 GT Software.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package ar.com.gtsoftware.dao;

import ar.com.gtsoftware.domain.Ofertas;
import ar.com.gtsoftware.domain.Ofertas_;
import ar.com.gtsoftware.domain.Sucursales_;
import ar.com.gtsoftware.search.OfertasSearchFilter;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Repository
public class OfertasFacade extends AbstractFacade<Ofertas, OfertasSearchFilter> {

    private static final String LIKE_FORMAT = "%%%s%%";

    private final EntityManager em;

    public OfertasFacade(EntityManager em) {
        super(Ofertas.class);
        this.em = em;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public Predicate createWhereFromSearchFilter(OfertasSearchFilter sf, CriteriaBuilder cb, Root<Ofertas> root) {
        Predicate p = null;

        if (BooleanUtils.isTrue(sf.getActivas())) {

            Predicate p1 = cb.between(cb.currentTimestamp(), root.get(Ofertas_.vigenciaDesde), root.get(Ofertas_.vigenciaHasta));
            p = appendAndPredicate(cb, p1, p);

        }

        if (sf.getIdSucursal() != null) {
            //IdSucursal en nulo aplica a todas las scucursales
            Predicate p1 = cb.equal(root.get(Ofertas_.idSucursal).get(Sucursales_.id), sf.getIdSucursal());
            Predicate p2 = cb.isNull(root.get(Ofertas_.idSucursal));
            Predicate pOr = cb.or(p1, p2);
            p = appendAndPredicate(cb, pOr, p);
        }

        if (StringUtils.isNotEmpty(sf.getTextoOferta())) {

            Predicate p1 = cb.like(root.get(Ofertas_.textoOferta), String.format(LIKE_FORMAT, sf.getTextoOferta().toUpperCase()));
            p = appendAndPredicate(cb, p1, p);
        }

        return p;
    }

}
