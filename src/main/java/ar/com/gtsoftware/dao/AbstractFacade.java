/*
 * Copyright 2018 GT Software.
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

import ar.com.gtsoftware.domain.GTEntity;
import ar.com.gtsoftware.search.AbstractSearchFilter;
import ar.com.gtsoftware.search.SortField;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractFacade<T extends GTEntity<?>, S extends AbstractSearchFilter> {

    private static final Logger LOG = Logger.getLogger(AbstractFacade.class.getName());
    private static final String LOAD_GRAPH = "javax.persistence.loadgraph";
    private final Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public AbstractFacade() {
        entityClass = null;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {
        constraintViolationsDetected(entity);
        getEntityManager().persist(entity);
    }

    public T edit(T entity) {
        constraintViolationsDetected(entity);
        return getEntityManager().merge(entity);
    }

    public void remove(T entity) {
        T toDelete = getEntityManager().find(entityClass, entity.getId());
        getEntityManager().remove(toDelete);
        getEntityManager().flush();
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public T find(Object id, String entityGraph) {
        return getEntityManager().find(entityClass, id, createHints(entityGraph));
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    protected abstract Predicate createWhereFromSearchFilter(S sf, CriteriaBuilder cb, Root<T> root);

    private Map<String, Object> createHints(S sf) {
        if (sf != null && sf.hasNamedEntityGraph()) {
            return createHints(sf.getNamedEntityGraph());
        }
        return Collections.emptyMap();
    }

    private Map<String, Object> createHints(String namedEntityGraph) {
        if (StringUtils.isNotEmpty(namedEntityGraph)) {
            Map<String, Object> hints = new HashMap<>();
            List<EntityGraph<? super T>> graphs = getEntityManager().getEntityGraphs(this.entityClass);
            for (EntityGraph<? super T> eg : graphs) {
                if (eg.getName().equalsIgnoreCase(namedEntityGraph)) {
                    hints.put(LOAD_GRAPH, eg);
                    break;
                }
            }
            return hints;

        }
        return Collections.emptyMap();
    }

    public List<T> findAllBySearchFilter(S sf) {
        return findBySearchFilter(sf, 0, countBySearchFilter(sf));
    }

    public List<T> findBySearchFilter(S sf, int firstResult, int maxResults) {

        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(entityClass);
        Root<T> root = cq.from(entityClass);
        cq.select(root);
        if (sf != null) {
            if (sf.hasFilter()) {
                Predicate p = createWhereFromSearchFilter(sf, cb, root);
                cq.where(p);
            }
            if (sf.hasOrderFields()) {
                cq.orderBy(createOrderFromSearchFilter(sf, root, cb));
            }
        }
        TypedQuery<T> q = getEntityManager().createQuery(cq);
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        Map<String, Object> hints = createHints(sf);

        if (!hints.isEmpty()) {
            hints.forEach((x, y) -> q.setHint(x, y));
        }
        return q.getResultList();

    }

    private List<Order> createOrderFromSearchFilter(S sf, Root<T> root, CriteriaBuilder cb) {
        List<Order> orders = new ArrayList<>();
        for (SortField sof : sf.getSortFields()) {
            Order ord;
            Path<Object> path;

            String[] fields = sof.getFieldName().split("\\.");
            if (fields.length != 0) {
                path = root.get(fields[0]);
                for (int i = 1; i < fields.length; i++) {
                    path = path.get(fields[i]);
                }
            } else {
                path = root.get(sof.getFieldName());
            }
            if (sof.isAscending()) {

                ord = cb.asc(path);
            } else {
                ord = cb.desc(path);
            }
            orders.add(ord);
        }
        return orders;
    }

    /**
     * Cuenta los elementos encontrados por el filtro de búsqueda.
     *
     * @param sf el filtro de búsqueda
     * @return la cantidad de elementos encontrados
     */
    public int countBySearchFilter(S sf) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        if (sf.hasFilter()) {
            Predicate p = createWhereFromSearchFilter(sf, getEntityManager().getCriteriaBuilder(), rt);
            cq.where(p);
        }
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    /**
     * Devuelve el primer elemento encontrado por el filtro de búsqueda.
     *
     * @param sf el filtro de búsuqeda
     * @return el primer elemento o null si no encuentra ninguno.
     */
    public T findFirstBySearchFilter(S sf) {
        List<T> results = findBySearchFilter(sf, 0, 1);
        if (CollectionUtils.isEmpty(results)) {
            return null;
        }
        return results.get(0);
    }

    /**
     * Retorna el OR entre los predicados pasados como parámetro
     *
     * @param cb el criteria builder
     * @param p1 el predicado 1
     * @param p2 el predicado 2
     * @return un predicado que une a p1 y p2 mediante un OR
     */
    protected Predicate appendOrPredicate(CriteriaBuilder cb, Predicate p1, Predicate p2) {
        validateAppendArguments(cb, p1, p2);
        if (p1 == null) {
            return p2;
        } else if (p2 == null) {
            return p1;
        }
        return cb.or(p1, p2);
    }

    /**
     * Retorna el AND entre los predicados pasados como parámetro
     *
     * @param cb el criteria builder
     * @param p1 el predicado 1
     * @param p2 el predicado 2
     * @return un predicado que une a p1 y p2 mediante un AND
     */
    protected Predicate appendAndPredicate(CriteriaBuilder cb, Predicate p1, Predicate p2) {
        validateAppendArguments(cb, p1, p2);
        if (p1 == null) {
            return p2;
        } else if (p2 == null) {
            return p1;
        }
        return cb.and(p1, p2);
    }

    /**
     * Método que valida los parámetros que reciben los métodos de append.
     *
     * @param cb el criteria builder
     * @param p1 el predicado 1
     * @param p2 el predicado 2
     */
    private void validateAppendArguments(@NotNull(message = "El CriteriaBuilder no puede ser null.") CriteriaBuilder cb,
                                         Predicate p1, Predicate p2) {
        if (p1 == null && p2 == null) {
            throw new IllegalArgumentException("Al menos unos de los predicados debe estar establecido.");
        }
    }

    /**
     * Crea o edita la entidad pasada como parámetro según sea necesario
     *
     * @param entity
     * @return T
     */
    public T createOrEdit(@NotNull T entity) {

        if (entity.isNew()) {
            create(entity);
        } else {
            return edit(entity);
        }
        return entity;
    }

    private boolean constraintViolationsDetected(T entity) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(entity);
        if (constraintViolations.size() > 0) {
            for (ConstraintViolation<T> cv : constraintViolations) {
                LOG.log(Level.SEVERE, "{0}.{1} {2}",
                        new Object[]{cv.getRootBeanClass().getName(), cv.getPropertyPath(), cv.getMessage()});
            }
            return true;
        } else {
            return false;
        }
    }
}
