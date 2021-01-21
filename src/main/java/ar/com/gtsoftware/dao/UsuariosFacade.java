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

import ar.com.gtsoftware.domain.Usuarios;
import ar.com.gtsoftware.domain.Usuarios_;
import ar.com.gtsoftware.search.UsuariosSearchFilter;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

/** @author Rodrigo M. Tato Rothamel <rotatomel@gmail.com> */
@Repository
public class UsuariosFacade extends AbstractFacade<Usuarios, UsuariosSearchFilter> {

  private final EntityManager em;

  public UsuariosFacade(EntityManager em) {
    super(Usuarios.class);
    this.em = em;
  }

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  @Override
  public Predicate createWhereFromSearchFilter(
      UsuariosSearchFilter usf, CriteriaBuilder cb, Root<Usuarios> root) {
    Predicate p = null;
    if (usf.getIdUsuario() != null) {
      p = cb.equal(root.get(Usuarios_.id), usf.getIdUsuario());
    }
    if (usf.getNombreUsuario() != null) {
      Predicate p1 =
          cb.like(
              root.get(Usuarios_.nombreUsuario),
              String.format("%%%s%%", usf.getNombreUsuario().toLowerCase()));
      p = appendOrPredicate(cb, p, p1);
    }
    if (usf.getLogin() != null) {
      Predicate p1 = cb.equal(root.get(Usuarios_.login), usf.getLogin());
      p = appendAndPredicate(cb, p, p1);
    }
    if (usf.getPassword() != null) {
      Predicate p1 = cb.equal(root.get(Usuarios_.password), usf.getPassword());
      p = appendOrPredicate(cb, p, p1);
    }
    if (usf.hasTextFilter()) {
      for (String s : usf.getText().toUpperCase().split(" ")) {

        Predicate p1 = cb.like(cb.upper(root.get(Usuarios_.login)), String.format("%%%s%%", s));
        Predicate p2 =
            cb.like(cb.upper(root.get(Usuarios_.nombreUsuario)), String.format("%%%s%%", s));

        p = appendOrPredicate(cb, p, p1);
        p = appendOrPredicate(cb, p, p2);
      }
    }
    return p;
  }
}
