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

import ar.com.gtsoftware.dao.CajasMovimientosFacade;
import ar.com.gtsoftware.dao.CajasTransferenciasFacade;
import ar.com.gtsoftware.domain.CajasMovimientos;
import ar.com.gtsoftware.domain.CajasTransferencias;
import ar.com.gtsoftware.dto.domain.CajasTransferenciasDto;
import ar.com.gtsoftware.mappers.CajasTransferenciasMapper;
import ar.com.gtsoftware.mappers.helper.CycleAvoidingMappingContext;
import ar.com.gtsoftware.search.CajasTransferenciasSearchFilter;
import ar.com.gtsoftware.service.BaseEntityService;
import ar.com.gtsoftware.service.CajasTransferenciasService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CajasTransferenciasServiceImpl
    extends BaseEntityService<
        CajasTransferenciasDto, CajasTransferenciasSearchFilter, CajasTransferencias>
    implements CajasTransferenciasService {

  private final CajasTransferenciasFacade facade;

  private final CajasTransferenciasMapper mapper;

  private final CajasMovimientosFacade cajasMovimientosFacade;

  @Override
  protected CajasTransferenciasFacade getFacade() {
    return facade;
  }

  @Override
  protected CajasTransferenciasMapper getMapper() {
    return mapper;
  }

  @Override
  public CajasTransferenciasDto generarTransferencia(CajasTransferenciasDto transfencia) {
    CajasTransferencias entity = mapper.dtoToEntity(transfencia, new CycleAvoidingMappingContext());
    facade.create(entity);

    String descMovimiento =
        String.format(
            "Transferencia Nro: %d desde caja: %s hacia caja: %s",
            entity.getId(),
            transfencia.getIdCajaOrigen().toString(),
            transfencia.getIdCajaDestino().toString());

    CajasMovimientos movOrigen = new CajasMovimientos();
    movOrigen.setFechaMovimiento(transfencia.getFechaTransferencia());
    movOrigen.setDescripcion(descMovimiento);
    movOrigen.setIdCaja(entity.getIdCajaOrigen());
    movOrigen.setMontoMovimiento(entity.getMonto().negate());
    cajasMovimientosFacade.create(movOrigen);

    CajasMovimientos movDestino = new CajasMovimientos();
    movDestino.setFechaMovimiento(transfencia.getFechaTransferencia());
    movDestino.setDescripcion(descMovimiento);
    movDestino.setIdCaja(entity.getIdCajaDestino());
    movDestino.setMontoMovimiento(entity.getMonto());
    cajasMovimientosFacade.create(movDestino);

    return mapper.entityToDto(entity, new CycleAvoidingMappingContext());
  }
}
