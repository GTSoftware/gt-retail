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

import ar.com.gtsoftware.entity.FiscalLibroIvaVentas;
import ar.com.gtsoftware.entity.FiscalLibroIvaVentas_;
import ar.com.gtsoftware.entity.FiscalPeriodosFiscales_;
import ar.com.gtsoftware.search.LibroIVASearchFilter;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

@Repository
public class FiscalLibroIvaVentasFacade
    extends AbstractFacade<FiscalLibroIvaVentas, LibroIVASearchFilter> {

  private final EntityManager em;

  public FiscalLibroIvaVentasFacade(EntityManager em) {
    super(FiscalLibroIvaVentas.class);
    this.em = em;
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  /**
   * Retorna la ultima factura registrada con la letra y el punto de venta pasados por parametro
   *
   * @param letra
   * @param puntoVenta
   * @return una factura
   */
  public FiscalLibroIvaVentas findUltimaFactura(String letra, String puntoVenta) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<FiscalLibroIvaVentas> cq = cb.createQuery(FiscalLibroIvaVentas.class);
    Root<FiscalLibroIvaVentas> registroIVA = cq.from(FiscalLibroIvaVentas.class);
    cq.select(registroIVA);
    Predicate p1 = cb.equal(registroIVA.get(FiscalLibroIvaVentas_.letraFactura), letra);
    Predicate p2 = cb.equal(registroIVA.get(FiscalLibroIvaVentas_.puntoVentaFactura), puntoVenta);
    // Predicate p3 = cb.equal(registroIVA.get(FiscalLibroIvaVentas_.codigoTipoComprobante),
    // tipoComp);

    cq.where(cb.and(p1, p2));
    cq.orderBy(cb.desc(registroIVA.get(FiscalLibroIvaVentas_.numeroFactura)));
    TypedQuery<FiscalLibroIvaVentas> q = em.createQuery(cq);
    q.setMaxResults(1);
    List<FiscalLibroIvaVentas> registroIVAList = q.getResultList();
    if (!registroIVAList.isEmpty()) {
      return registroIVAList.get(0);
    }
    return null;
  }

  @Override
  public Predicate createWhereFromSearchFilter(
      LibroIVASearchFilter ivavsf, CriteriaBuilder cb, Root<FiscalLibroIvaVentas> root) {
    Predicate p = null;
    if (ivavsf.getIdPeriodo() != null) {
      Predicate p1 =
          cb.equal(
              root.get(FiscalLibroIvaVentas_.idPeriodoFiscal).get(FiscalPeriodosFiscales_.id),
              ivavsf.getIdPeriodo());
      p = appendAndPredicate(cb, p1, p);
    }
    if (ivavsf.hasFechasDesdeHasta()) {
      Predicate p1 =
          cb.between(
              root.get(FiscalLibroIvaVentas_.fechaFactura),
              ivavsf.getFechaDesde(),
              ivavsf.getFechaHasta());
      p = appendAndPredicate(cb, p1, p);
    }
    if (ivavsf.getAnuladas() != null) {
      Predicate p1 = cb.equal(root.get(FiscalLibroIvaVentas_.anulada), ivavsf.getAnuladas());
      p = appendAndPredicate(cb, p1, p);
    }
    return p;
  }
}
