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

package ar.com.gtsoftware.service;

import ar.com.gtsoftware.dao.AbstractFacade;
import ar.com.gtsoftware.entity.GTEntity;
import ar.com.gtsoftware.mappers.GenericMapper;
import ar.com.gtsoftware.mappers.helper.CycleAvoidingMappingContext;
import ar.com.gtsoftware.search.AbstractSearchFilter;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotNull;

public abstract class BaseEntityService<D, S extends AbstractSearchFilter, E extends GTEntity<?>>
    implements EntityService<D, S> {

  protected abstract AbstractFacade<E, S> getFacade();

  protected abstract GenericMapper<E, D> getMapper();

  @Override
  public D createOrEdit(@NotNull D dto) {
    E entity =
        getFacade().createOrEdit(getMapper().dtoToEntity(dto, new CycleAvoidingMappingContext()));
    return getMapper().entityToDto(entity, new CycleAvoidingMappingContext());
  }

  @Override
  public void remove(@NotNull D dto) {
    getFacade().remove(getMapper().dtoToEntity(dto, new CycleAvoidingMappingContext()));
  }

  @Override
  public int countBySearchFilter(@NotNull S sf) {
    return getFacade().countBySearchFilter(sf);
  }

  @Override
  public List<D> findAll() {
    return getMapper().entitiesToDtos(getFacade().findAll(), new CycleAvoidingMappingContext());
  }

  @Override
  public D find(@NotNull Serializable id) {
    return getMapper().entityToDto(getFacade().find(id), new CycleAvoidingMappingContext());
  }

  @Override
  public D findFirstBySearchFilter(@NotNull S sf) {
    return getMapper()
        .entityToDto(getFacade().findFirstBySearchFilter(sf), new CycleAvoidingMappingContext());
  }

  @Override
  public List<D> findBySearchFilter(@NotNull S sf, int firstResult, int maxResults) {
    return getMapper()
        .entitiesToDtos(
            getFacade().findBySearchFilter(sf, firstResult, maxResults),
            new CycleAvoidingMappingContext());
  }

  @Override
  public List<D> findAllBySearchFilter(@NotNull S sf) {
    return getMapper()
        .entitiesToDtos(getFacade().findAllBySearchFilter(sf), new CycleAvoidingMappingContext());
  }
}
