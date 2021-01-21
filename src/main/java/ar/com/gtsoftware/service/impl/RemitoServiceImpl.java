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

import ar.com.gtsoftware.dao.*;
import ar.com.gtsoftware.domain.Remito;
import ar.com.gtsoftware.domain.RemitoDetalle;
import ar.com.gtsoftware.domain.RemitoRecepcion;
import ar.com.gtsoftware.dto.ProductoMovimiento;
import ar.com.gtsoftware.dto.domain.RemitoDto;
import ar.com.gtsoftware.mappers.ProductosMapper;
import ar.com.gtsoftware.mappers.RemitoMapper;
import ar.com.gtsoftware.mappers.helper.CycleAvoidingMappingContext;
import ar.com.gtsoftware.search.ProductoXDepositoSearchFilter;
import ar.com.gtsoftware.search.RemitoDetalleSearchFilter;
import ar.com.gtsoftware.search.RemitoSearchFilter;
import ar.com.gtsoftware.search.SortField;
import ar.com.gtsoftware.service.BaseEntityService;
import ar.com.gtsoftware.service.RemitoService;
import ar.com.gtsoftware.utils.BusinessDateUtils;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RemitoServiceImpl extends BaseEntityService<RemitoDto, RemitoSearchFilter, Remito>
    implements RemitoService {

  private final RemitoFacade facade;

  private final RemitoDetalleFacade remitoDetalleFacade;

  private final RemitoMapper mapper;

  private final UsuariosFacade usuariosFacade;
  private final RemitoTipoMovimientoFacade tipoMovimientoFacade;
  private final DepositosFacade depositosFacade;
  private final BusinessDateUtils dateUtils;
  private final ProductosFacade productosFacade;
  private final PersonasFacade personasFacade;
  private final ProductosMapper productosMapper;
  private final ProductoXDepositoFacade stockFacade;

  @Override
  protected RemitoFacade getFacade() {
    return facade;
  }

  @Override
  protected RemitoMapper getMapper() {
    return mapper;
  }

  @Override
  @Transactional
  public Long guardarRemito(RemitoDto remitoDto) {
    Remito remito = mapper.dtoToEntity(remitoDto, new CycleAvoidingMappingContext());
    completeInformation(remito);
    // TODO validar cantidades

    final LocalDateTime now = dateUtils.getCurrentDateTime();

    remito.setFechaAlta(now);
    agregarRemitoRecepcion(remito);

    return facade.createOrEdit(remito).getId();
  }

  private void agregarRemitoRecepcion(Remito remito) {
    RemitoRecepcion recepcion = new RemitoRecepcion();
    recepcion.setRemito(remito);
    recepcion.setFecha(remito.getFechaAlta());
    recepcion.setIdUsuario(remito.getIdUsuario());

    if (remito.getIsDestinoInterno()) {
      recepcion.setIdDeposito(remito.getIdDestinoPrevistoInterno());
    } else {
      recepcion.setIdPersona(remito.getIdDestinoPrevistoExterno());
    }

    remito.setRemitoRecepcionesList(Collections.singletonList(recepcion));
  }

  private void completeInformation(Remito remito) {
    remito.setIdUsuario(usuariosFacade.find(remito.getIdUsuario().getId()));
    remito.setRemitoTipoMovimiento(
        tipoMovimientoFacade.find(remito.getRemitoTipoMovimiento().getId()));

    if (remito.getIsDestinoInterno()) {
      remito.setIdDestinoPrevistoInterno(
          depositosFacade.find(remito.getIdDestinoPrevistoInterno().getId()));
    } else {
      remito.setIdDestinoPrevistoExterno(
          personasFacade.find(remito.getIdDestinoPrevistoExterno().getId()));
    }

    if (remito.getIsOrigenInterno()) {
      remito.setIdOrigenInterno(depositosFacade.find(remito.getIdOrigenInterno().getId()));
    } else {
      remito.setIdOrigenExterno(personasFacade.find(remito.getIdOrigenExterno().getId()));
    }

    completeRemitoDetalles(remito);
  }

  private void completeRemitoDetalles(Remito remito) {
    for (RemitoDetalle rd : remito.getDetalleList()) {
      rd.setRemitoCabecera(remito);
      rd.setIdProducto(productosFacade.find(rd.getIdProducto().getId()));
    }
  }

  @Override
  public List<ProductoMovimiento> getMovimientosProducto(RemitoDetalleSearchFilter sf) {
    final BigDecimal stockActual = getStockActual(sf.getIdProducto(), sf.getIdDepositoMovimiento());
    sf.setSortFields(Collections.singletonList(new SortField("remitoCabecera.fechaAlta", false)));
    final List<RemitoDetalle> remitoDetalles = remitoDetalleFacade.findAllBySearchFilter(sf);

    BigDecimal stockMovimiento = stockActual;
    List<ProductoMovimiento> productoMovimientos = new ArrayList<>(remitoDetalles.size());
    for (RemitoDetalle rd : remitoDetalles) {
      final Remito remitoCabecera = rd.getRemitoCabecera();

      productoMovimientos.add(
          ProductoMovimiento.builder()
              .cantidad(rd.getCantidad())
              .remito(mapper.entityToDto(remitoCabecera, new CycleAvoidingMappingContext()))
              .producto(
                  productosMapper.entityToDto(
                      rd.getIdProducto(), new CycleAvoidingMappingContext()))
              .stockParcial(stockMovimiento)
              .build());

      if (remitoCabecera.getIsOrigenInterno() && !remitoCabecera.getIsDestinoInterno()) {
        stockMovimiento = stockMovimiento.add(rd.getCantidad());
      } else if (!remitoCabecera.getIsOrigenInterno() && remitoCabecera.getIsDestinoInterno()) {
        stockMovimiento = stockMovimiento.subtract(rd.getCantidad());
      }
      if (remitoCabecera.getIsDestinoInterno() && remitoCabecera.getIsOrigenInterno()) {
        if (remitoCabecera.getIdOrigenInterno().getId().equals(sf.getIdDepositoMovimiento())) {
          stockMovimiento = stockMovimiento.add(rd.getCantidad());
        } else {
          stockMovimiento = stockMovimiento.subtract(rd.getCantidad());
        }
      }
    }

    return productoMovimientos;
  }

  private BigDecimal getStockActual(Long idProducto, Long idDepositoMovimiento) {
    ProductoXDepositoSearchFilter stockSf = new ProductoXDepositoSearchFilter();
    stockSf.setIdDeposito(idDepositoMovimiento);
    stockSf.setIdProducto(idProducto);

    return stockFacade.getStockBySearchFilter(stockSf);
  }
}
