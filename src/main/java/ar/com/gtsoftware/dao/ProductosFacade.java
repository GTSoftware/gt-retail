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
import ar.com.gtsoftware.search.ProductosSearchFilter;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class ProductosFacade extends AbstractFacade<Productos, ProductosSearchFilter> {

  private static final String SPACE = "\\s";
  private static final String LIKE = "%%%s%%";

  private final EntityManager em;

  public ProductosFacade(EntityManager em) {
    super(Productos.class);
    this.em = em;
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  @Override
  public Predicate createWhereFromSearchFilter(
      ProductosSearchFilter psf, CriteriaBuilder cb, Root<Productos> root) {

    Predicate p = null;
    if (psf.getIdProducto() != null) {
      p = cb.equal(root.get(Productos_.id), psf.getIdProducto());
    }
    if (StringUtils.isNotEmpty(psf.getCodigoPropio())) {
      p = cb.equal(root.get(Productos_.codigoPropio), psf.getCodigoPropio());
    }
    if (StringUtils.isNotEmpty(psf.getCodigoFabrica())) {
      p = cb.equal(root.get(Productos_.codigoFabricante), psf.getCodigoFabrica());
    }
    if (StringUtils.isNotEmpty(psf.getTxt())) {

      if (StringUtils.isNumeric(psf.getTxt())) {

        Predicate pId = cb.equal(root.get(Productos_.id), Long.parseLong(psf.getTxt()));
        Predicate pCodigo = cb.like(root.get(Productos_.codigoPropio), psf.getTxt());

        p = appendOrPredicate(cb, p, pId);
        p = appendOrPredicate(cb, p, pCodigo);

      } else {

        if (psf.isBuscarEnTodosLados()) {

          for (String s : psf.getTxt().toUpperCase().split(SPACE)) {
            Predicate pTxt = null;
            String likeS = String.format(LIKE, s);

            Predicate p1 = cb.like(root.get(Productos_.descripcion), likeS);
            Predicate p2 =
                cb.like(root.get(Productos_.idRubro).get(ProductosRubros_.nombreRubro), likeS);
            Predicate p3 =
                cb.like(
                    root.get(Productos_.idSubRubro).get(ProductosSubRubros_.nombreSubRubro), likeS);
            Predicate p4 =
                cb.like(root.get(Productos_.idMarca).get(ProductosMarcas_.nombreMarca), likeS);
            Predicate pCod = cb.like(root.get(Productos_.codigoPropio), likeS);
            Predicate pCodFab = cb.like(root.get(Productos_.codigoFabricante), likeS);

            pTxt = appendOrPredicate(cb, pTxt, pCod);
            pTxt = appendOrPredicate(cb, pTxt, pCodFab);
            pTxt = appendOrPredicate(cb, pTxt, p1);
            pTxt = appendOrPredicate(cb, pTxt, p2);
            pTxt = appendOrPredicate(cb, pTxt, p3);
            pTxt = appendOrPredicate(cb, pTxt, p4);

            p = appendAndPredicate(cb, p, pTxt);
          }
        } else {
          String texto = psf.getTxt().toUpperCase().replaceAll(SPACE, "%");
          String descLike = String.format("%s%%", texto);
          Predicate p1 = cb.like(root.get(Productos_.descripcion), descLike);
          p = appendAndPredicate(cb, p, p1);
        }
      }
    }
    if (psf.getIdProveedorHabitual() != null) {
      Predicate p1 =
          cb.equal(
              root.get(Productos_.idProveedorHabitual).get(Personas_.id),
              psf.getIdProveedorHabitual());
      p = appendAndPredicate(cb, p, p1);
    }
    if (psf.getPuedeComprarse() != null) {
      Predicate p1 =
          cb.equal(
              root.get(Productos_.idTipoProveeduria).get(ProductosTiposProveeduria_.puedeComprarse),
              psf.getPuedeComprarse());
      p = appendAndPredicate(cb, p, p1);
    }
    if (psf.getPuedeVenderse() != null) {
      Predicate p1 =
          cb.equal(
              root.get(Productos_.idTipoProveeduria).get(ProductosTiposProveeduria_.puedeVenderse),
              psf.getPuedeVenderse());
      p = appendAndPredicate(cb, p, p1);
    }
    if (psf.getIdRubro() != null) {
      Predicate p1 =
          cb.equal(root.get(Productos_.idRubro).get(ProductosRubros_.id), psf.getIdRubro());
      p = appendAndPredicate(cb, p, p1);
    }
    if (psf.getIdSubRubro() != null) {
      Predicate p1 =
          cb.equal(
              root.get(Productos_.idSubRubro).get(ProductosSubRubros_.id), psf.getIdSubRubro());
      p = appendAndPredicate(cb, p, p1);
    }
    if (psf.getActivo() != null) {
      Predicate p1 = cb.equal(root.get(Productos_.activo), psf.getActivo());
      p = appendAndPredicate(cb, p, p1);
    }
    // TODO para cuando se implemente el stock por depósito
    //        if (psf.getConStockEnDeposito() != null) {
    //            Predicate p1 =
    // cb.isTrue(root.get(Productos_.idTipoProveeduria).get(ProductosTiposProveeduria_.controlStock));
    //
    //            p = appendAndPredicate(cb, p, p1);
    //        }
    if (psf.getIdTipoProveeduria() != null) {
      Predicate p1 =
          cb.equal(
              root.get(Productos_.idTipoProveeduria).get(ProductosTiposProveeduria_.id),
              psf.getIdTipoProveeduria());
      p = appendAndPredicate(cb, p, p1);
    }
    if (psf.getIdMarca() != null) {
      Predicate p1 =
          cb.equal(root.get(Productos_.idMarca).get(ProductosMarcas_.id), psf.getIdMarca());
      p = appendAndPredicate(cb, p, p1);
    }
    if (psf.getConStock() != null) {
      // Subquery de existencias en depósitos

      Predicate pstk = null;
      if (psf.getConStock()) {
        Subquery<Long> subQStk = cb.createQuery().subquery(Long.class);
        Root<ProductoXDeposito> fromSubQ = subQStk.from(ProductoXDeposito.class);

        subQStk.select(fromSubQ.get(ProductoXDeposito_.id));
        Predicate ps1 = cb.gt(fromSubQ.get(ProductoXDeposito_.stock), 0);
        Predicate ps2 = cb.equal(fromSubQ.get(ProductoXDeposito_.producto), root);
        subQStk.where(cb.and(ps1, ps2));

        Predicate p1 = cb.exists(subQStk);
        Predicate p2 =
            cb.isFalse(
                root.get(Productos_.idTipoProveeduria)
                    .get(ProductosTiposProveeduria_.controlStock));
        pstk = appendOrPredicate(cb, p1, p2);
      }
      p = appendAndPredicate(cb, p, pstk);
    }

    // TODO agregar un not exists sobre la tabla de stock para traer productos sin movimientos
    if (psf.getStockDebajoMinimo() != null) {
      Predicate pstk = null;
      if (psf.getStockDebajoMinimo()) {
        Subquery<Long> subQStk = cb.createQuery().subquery(Long.class);

        Root<ProductoXDeposito> fromSubQ = subQStk.from(ProductoXDeposito.class);

        subQStk.select(fromSubQ.get(ProductoXDeposito_.producto).get(Productos_.id));
        subQStk = subQStk.groupBy(fromSubQ.get(ProductoXDeposito_.producto).get(Productos_.id));

        Predicate ps2 = cb.equal(fromSubQ.get(ProductoXDeposito_.producto), root);

        Predicate p1 =
            cb.lessThanOrEqualTo(
                cb.sum(fromSubQ.get(ProductoXDeposito_.stock)), root.get(Productos_.stockMinimo));

        subQStk = subQStk.where(ps2);
        subQStk = subQStk.having(p1);

        pstk = cb.exists(subQStk);

        Subquery<Long> subQSinMov = cb.createQuery().subquery(Long.class);
        Root<ProductoXDeposito> fromSubQSinMov = subQSinMov.from(ProductoXDeposito.class);
        subQSinMov.select(fromSubQSinMov.get(ProductoXDeposito_.id));
        subQSinMov.where(cb.equal(fromSubQSinMov.get(ProductoXDeposito_.producto), root));

        pstk = cb.or(pstk, cb.not(cb.exists(subQSinMov)));
      }
      p = appendAndPredicate(cb, p, pstk);
    }

    if (psf.getLlevaControlStock() != null) {
      Predicate p1 =
          cb.equal(
              root.get(Productos_.idTipoProveeduria).get(ProductosTiposProveeduria_.controlStock),
              psf.getLlevaControlStock());
      p = appendAndPredicate(cb, p, p1);
    }

    return p;
  }
}
