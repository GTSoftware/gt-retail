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

package ar.com.gtsoftware.service.impl;

import ar.com.gtsoftware.dao.OfertasFacade;
import ar.com.gtsoftware.domain.Ofertas;
import ar.com.gtsoftware.mappers.OfertasMapper;
import ar.com.gtsoftware.rules.OfertaDto;
import ar.com.gtsoftware.search.OfertasSearchFilter;
import ar.com.gtsoftware.service.BaseEntityService;
import ar.com.gtsoftware.service.OfertasService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OfertasServiceImpl extends BaseEntityService<OfertaDto, OfertasSearchFilter, Ofertas>
    implements OfertasService {

  private final OfertasFacade facade;
  private final OfertasMapper mapper;

  @Override
  protected OfertasFacade getFacade() {
    return facade;
  }

  @Override
  protected OfertasMapper getMapper() {
    return mapper;
  }
}
