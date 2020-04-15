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

import ar.com.gtsoftware.dao.ProductoXDepositoFacade;
import ar.com.gtsoftware.domain.ProductoXDeposito;
import ar.com.gtsoftware.dto.domain.ProductoXDepositoDto;
import ar.com.gtsoftware.mappers.ProductoXDepositoMapper;
import ar.com.gtsoftware.search.ProductoXDepositoSearchFilter;
import ar.com.gtsoftware.service.BaseEntityService;
import ar.com.gtsoftware.service.ProductoXDepositoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProductoXDepositoServiceImpl
        extends BaseEntityService<ProductoXDepositoDto, ProductoXDepositoSearchFilter, ProductoXDeposito>
        implements ProductoXDepositoService {


    private final ProductoXDepositoFacade facade;

    private final ProductoXDepositoMapper mapper;

    @Override
    public ProductoXDepositoDto createOrEdit(@NotNull ProductoXDepositoDto dto) {
        throw new UnsupportedOperationException("No se puede actualizar esta entidad directamente.");
    }

    @Override
    public void remove(@NotNull ProductoXDepositoDto dto) {
        throw new UnsupportedOperationException("No se puede actualizar esta entidad directamente.");
    }

    @Override
    protected ProductoXDepositoFacade getFacade() {
        return facade;
    }

    @Override
    protected ProductoXDepositoMapper getMapper() {
        return mapper;
    }

    @Override
    public BigDecimal getStockBySearchFilter(ProductoXDepositoSearchFilter sf) {
        return facade.getStockBySearchFilter(sf);
    }
}
