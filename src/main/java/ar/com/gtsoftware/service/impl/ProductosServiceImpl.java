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

import ar.com.gtsoftware.dto.domain.ProductosDto;
import ar.com.gtsoftware.dao.ProductoXDepositoFacade;
import ar.com.gtsoftware.dao.ProductosFacade;
import ar.com.gtsoftware.dao.ProductosPreciosFacade;
import ar.com.gtsoftware.mappers.ProductosMapper;
import ar.com.gtsoftware.domain.Productos;
import ar.com.gtsoftware.domain.ProductosPrecios;
import ar.com.gtsoftware.search.ProductoXDepositoSearchFilter;
import ar.com.gtsoftware.search.ProductosPreciosSearchFilter;
import ar.com.gtsoftware.search.ProductosSearchFilter;
import ar.com.gtsoftware.service.BaseEntityService;
import ar.com.gtsoftware.service.ProductosService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductosServiceImpl
        extends BaseEntityService<ProductosDto, ProductosSearchFilter, Productos>
        implements ProductosService {


    private final ProductosFacade facade;

    private final ProductosPreciosFacade preciosFacade;
    private final ProductoXDepositoFacade stockFacade;

    private final ProductosMapper mapper;

    @Override
    protected ProductosFacade getFacade() {
        return facade;
    }

    @Override
    protected ProductosMapper getMapper() {
        return mapper;
    }

    @Override
    public List<ProductosDto> findBySearchFilter(@NotNull ProductosSearchFilter sf, int firstResult, int maxResults) {
        List<ProductosDto> productosDtos = super.findBySearchFilter(sf, firstResult, maxResults);
        establecerPrecioYStock(productosDtos, sf);
        return productosDtos;

    }

    private void establecerPrecioYStock(List<ProductosDto> productosDtos, @NotNull ProductosSearchFilter sf) {
        if (sf.getIdListaPrecio() != null) {
            ProductosPreciosSearchFilter preciosSF = ProductosPreciosSearchFilter.builder().idListaPrecios(sf.getIdListaPrecio())
                    .build();
            ProductoXDepositoSearchFilter stockSf = new ProductoXDepositoSearchFilter();
            for (ProductosDto prod : productosDtos) {
                stockSf.setIdProducto(prod.getId());
                preciosSF.setIdProducto(prod.getId());

                ProductosPrecios productosPrecios = preciosFacade.findFirstBySearchFilter(preciosSF);
                BigDecimal precioVenta = productosPrecios == null ? null : productosPrecios.getPrecio();
                prod.setPrecioVenta(precioVenta);

                if (prod.getIdTipoProveeduria().isControlStock()) {
                    BigDecimal stockTotal = stockFacade.getStockBySearchFilter(stockSf);
                    prod.setStockActual(stockTotal);

                    if (sf.getIdSucursal() != null) {
                        stockSf.setIdSucursal(sf.getIdSucursal());
                        BigDecimal stockSucursal = stockFacade.getStockBySearchFilter(stockSf);
                        prod.setStockActualEnSucursal(stockSucursal);
                    }
                }
            }
        }
    }

    @Override
    public ProductosDto findFirstBySearchFilter(@NotNull ProductosSearchFilter sf) {
        ProductosDto productosDto = super.findFirstBySearchFilter(sf);
        if (productosDto != null) {
            establecerPrecioYStock(Collections.singletonList(productosDto), sf);
        }
        return productosDto;
    }
}
