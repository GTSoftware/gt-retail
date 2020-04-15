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

import ar.com.gtsoftware.dao.LegalTiposPersoneriaFacade;
import ar.com.gtsoftware.domain.LegalTiposPersoneria;
import ar.com.gtsoftware.dto.domain.LegalTiposPersoneriaDto;
import ar.com.gtsoftware.mappers.LegalTiposPersoneriaMapper;
import ar.com.gtsoftware.search.AbstractSearchFilter;
import ar.com.gtsoftware.service.BaseEntityService;
import ar.com.gtsoftware.service.LegalTiposPersoneriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LegalTiposPersoneriaServiceImpl
        extends BaseEntityService<LegalTiposPersoneriaDto, AbstractSearchFilter, LegalTiposPersoneria>
        implements LegalTiposPersoneriaService {

    private final LegalTiposPersoneriaMapper mapper;

    private final LegalTiposPersoneriaFacade facade;

    @Override
    protected LegalTiposPersoneriaFacade getFacade() {
        return facade;
    }

    @Override
    protected LegalTiposPersoneriaMapper getMapper() {
        return mapper;
    }
}
