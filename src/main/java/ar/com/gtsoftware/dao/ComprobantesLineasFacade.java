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

import ar.com.gtsoftware.entity.Comprobantes;
import ar.com.gtsoftware.entity.ComprobantesLineas;
import ar.com.gtsoftware.entity.ComprobantesLineas_;
import ar.com.gtsoftware.search.AbstractSearchFilter;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

/**
 * @author rodrigo
 */
@Repository
public class ComprobantesLineasFacade
    extends AbstractFacade<ComprobantesLineas, AbstractSearchFilter> {

  private final EntityManager em;

  public ComprobantesLineasFacade(EntityManager em) {
    super(ComprobantesLineas.class);
    this.em = em;
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public List<ComprobantesLineas> findVentasLineas(Comprobantes comp) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<ComprobantesLineas> cq = cb.createQuery(ComprobantesLineas.class);
    Root<ComprobantesLineas> lineaVenta = cq.from(ComprobantesLineas.class);
    cq.select(lineaVenta);
    Predicate p = cb.equal(lineaVenta.get(ComprobantesLineas_.idComprobante), comp);
    cq.where(p);
    TypedQuery<ComprobantesLineas> q = em.createQuery(cq);
    return q.getResultList();
  }

  @Override
  public Predicate createWhereFromSearchFilter(
      AbstractSearchFilter sf, CriteriaBuilder cb, Root<ComprobantesLineas> root) {
    throw new UnsupportedOperationException(
        "Not supported yet."); // To change body of generated methods, choose Tools | Templates.
  }
}
