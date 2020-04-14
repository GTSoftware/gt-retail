/*
 * Copyright 2016 GT Software.
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

import ar.com.gtsoftware.domain.AFIPAuthServices;
import ar.com.gtsoftware.domain.AFIPAuthServices_;
import ar.com.gtsoftware.search.AFIPAuthServicesSearchFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;

@Repository
public class AFIPAuthServicesFacade extends AbstractFacade<AFIPAuthServices, AFIPAuthServicesSearchFilter> {


    private final EntityManager em;

    public AFIPAuthServicesFacade(EntityManager em) {
        super(AFIPAuthServices.class);
        this.em = em;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public Predicate createWhereFromSearchFilter(AFIPAuthServicesSearchFilter asf, CriteriaBuilder cb, Root<AFIPAuthServices> root) {

        Predicate p = null;
        if (StringUtils.isNotEmpty(asf.getService())) {
            p = cb.equal(root.get(AFIPAuthServices_.nombreServicio), asf.getService());
        }

        if (asf.getNoExpirado() != null) {
            Predicate p1 = cb.equal(cb.greaterThan(root.<Date>get(AFIPAuthServices_.fechaExpiracion),
                    cb.currentTimestamp()), asf.getNoExpirado());
            p = appendAndPredicate(cb, p, p1);
        }
        return p;

    }

}
