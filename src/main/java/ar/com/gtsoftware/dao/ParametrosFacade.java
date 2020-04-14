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

import ar.com.gtsoftware.domain.Parametros;
import ar.com.gtsoftware.domain.Parametros_;
import ar.com.gtsoftware.search.AbstractSearchFilter;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;

@Repository
public class ParametrosFacade extends AbstractFacade<Parametros, AbstractSearchFilter> {


    private final EntityManager em;

    public ParametrosFacade(EntityManager em) {
        super(Parametros.class);
        this.em = em;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Busca el parámetro con exactamente el nombre especificado
     *
     * @param nombre
     * @return
     */
    public Parametros findParametroByName(String nombre) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Parametros> cq = cb.createQuery(Parametros.class);
        Root<Parametros> parametro = cq.from(Parametros.class);
        cq.select(parametro);
        Predicate p = cb.equal(parametro.get(Parametros_.nombreParametro), nombre);
        cq.where(p);
        TypedQuery<Parametros> q = em.createQuery(cq);

        List<Parametros> paramList = q.getResultList();
        if (!paramList.isEmpty()) {
            return paramList.get(0);
        }
        return null;
    }

    /**
     * Busca los parámetros que coincidan con txt
     *
     * @param txt
     * @return
     */
    public List<Parametros> findParametros(String txt) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Parametros> cq = cb.createQuery(Parametros.class);
        Root<Parametros> parametro = cq.from(Parametros.class);
        cq.select(parametro);
        Predicate p1 = cb.like(parametro.get(Parametros_.nombreParametro), String.format("%%%s%%", txt));
        Predicate p2 = cb.like(parametro.get(Parametros_.descripcionParametro), String.format("%%%s%%", txt));
        cq.where(cb.or(p1, p2));
        TypedQuery<Parametros> q = em.createQuery(cq);

        List<Parametros> paramList = q.getResultList();
        if (!paramList.isEmpty()) {
            return paramList;
        }
        return Collections.emptyList();
    }

    @Override
    public Predicate createWhereFromSearchFilter(AbstractSearchFilter sf, CriteriaBuilder cb, Root<Parametros> root) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
