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

import ar.com.gtsoftware.api.request.BatchPricingUpdateRequest;
import ar.com.gtsoftware.api.request.ProductPercent;
import ar.com.gtsoftware.dao.ProductoXDepositoFacade;
import ar.com.gtsoftware.dao.ProductosFacade;
import ar.com.gtsoftware.dao.ProductosPreciosFacade;
import ar.com.gtsoftware.dao.ProductosTiposPorcentajesFacade;
import ar.com.gtsoftware.domain.Productos;
import ar.com.gtsoftware.domain.ProductosPorcentajes;
import ar.com.gtsoftware.domain.ProductosPrecios;
import ar.com.gtsoftware.dto.domain.ProductosDto;
import ar.com.gtsoftware.mappers.ProductosMapper;
import ar.com.gtsoftware.search.ProductoXDepositoSearchFilter;
import ar.com.gtsoftware.search.ProductosPreciosSearchFilter;
import ar.com.gtsoftware.search.ProductosSearchFilter;
import ar.com.gtsoftware.service.BaseEntityService;
import ar.com.gtsoftware.service.ProductosService;
import ar.com.gtsoftware.utils.BusinessDateUtils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductosServiceImpl
    extends BaseEntityService<ProductosDto, ProductosSearchFilter, Productos>
    implements ProductosService {

  private static final BigDecimal CIEN = new BigDecimal(100);
  private final ProductosFacade facade;
  private final ProductosPreciosFacade preciosFacade;
  private final ProductosTiposPorcentajesFacade tiposPorcentajesFacade;
  private final ProductoXDepositoFacade stockFacade;
  private final ProductosMapper mapper;
  private final BusinessDateUtils dateUtils;

  @Override
  protected ProductosFacade getFacade() {
    return facade;
  }

  @Override
  protected ProductosMapper getMapper() {
    return mapper;
  }

  @Override
  public List<ProductosDto> findBySearchFilter(
      @NotNull ProductosSearchFilter sf, int firstResult, int maxResults) {
    List<ProductosDto> productosDtos = super.findBySearchFilter(sf, firstResult, maxResults);
    establecerPrecioYStock(productosDtos, sf);
    return productosDtos;
  }

  private void establecerPrecioYStock(
      List<ProductosDto> productosDtos, @NotNull ProductosSearchFilter sf) {
    if (sf.getIdListaPrecio() != null) {
      ProductosPreciosSearchFilter preciosSF =
          ProductosPreciosSearchFilter.builder().idListaPrecios(sf.getIdListaPrecio()).build();
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

  @Override
  @Transactional
  public void updatePrices(BatchPricingUpdateRequest batchUpdateRequest) {
    final List<Productos> productos =
        facade.findAllBySearchFilter(batchUpdateRequest.getSearchFilter());

    final LocalDateTime today = dateUtils.getCurrentDateTime();

    for (Productos producto : productos) {
      if (batchUpdateRequest.getCostUpdatePercent() != null) {
        BigDecimal costoAdquisicionNeto = producto.getCostoAdquisicionNeto();
        costoAdquisicionNeto =
            costoAdquisicionNeto.add(
                costoAdquisicionNeto.multiply(
                    batchUpdateRequest.getCostUpdatePercent().divide(CIEN)));
        producto.setCostoAdquisicionNeto(costoAdquisicionNeto);
      }
      final List<ProductosPorcentajes> porcentajes = producto.getPorcentajes();
      if (CollectionUtils.isNotEmpty(batchUpdateRequest.getPercentsToDelete())) {
        for (ProductPercent toRemove : batchUpdateRequest.getPercentsToDelete()) {
          porcentajes.removeIf(
              p ->
                  p.getIdTipoPorcentaje().getId().equals(toRemove.getPercentTypeId())
                      && p.getValor().compareTo(toRemove.getPercentValue()) == 0);
        }
      }
      if (CollectionUtils.isNotEmpty(batchUpdateRequest.getPercentsToAdd())) {
        for (ProductPercent toAdd : batchUpdateRequest.getPercentsToAdd()) {
          ProductosPorcentajes pp = new ProductosPorcentajes();
          pp.setIdProducto(producto);
          pp.setIdTipoPorcentaje(tiposPorcentajesFacade.find(toAdd.getPercentTypeId()));
          pp.setValor(toAdd.getPercentValue());
          pp.setFechaModificacion(today);
          porcentajes.add(pp);
        }
      }

      updateSalePrices(producto, today);
      producto.setFechaUltimaModificacion(today);

      facade.edit(producto);
    }
  }

  private void updateSalePrices(Productos producto, LocalDateTime today) {
    BigDecimal costoAdquisicionNeto = producto.getCostoAdquisicionNeto();
    List<ProductosPorcentajes> porcentajes = producto.getPorcentajes();
    BigDecimal costoFinal = costoAdquisicionNeto;
    for (ProductosPorcentajes pp : porcentajes) {
      if (pp.getIdTipoPorcentaje().isPorcentaje()) {
        costoFinal = costoFinal.add(costoFinal.multiply(pp.getValor().divide(CIEN)));
      } else {
        costoFinal = costoFinal.add(pp.getValor());
      }
    }
    producto.setCostoFinal(costoFinal);
    if (producto.getPrecios() != null) {
      BigDecimal coeficienteIVA =
          producto.getIdAlicuotaIva().getValorAlicuota().divide(CIEN).add(BigDecimal.ONE);
      for (ProductosPrecios pp : producto.getPrecios()) {
        BigDecimal utilidad = pp.getUtilidad().divide(CIEN);
        pp.setNeto(costoFinal.add(costoFinal.multiply(utilidad)));
        pp.setFechaModificacion(today);
        pp.setPrecio(pp.getNeto().multiply(coeficienteIVA).setScale(2, RoundingMode.HALF_UP));
      }
    }
  }
}
