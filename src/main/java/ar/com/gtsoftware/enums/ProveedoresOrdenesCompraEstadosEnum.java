/*
 * Copyright 2020 GT Software.
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

package ar.com.gtsoftware.enums;

import ar.com.gtsoftware.dto.domain.ProveedoresOrdenesCompraEstadosDto;
import lombok.Getter;

@Getter
public enum ProveedoresOrdenesCompraEstadosEnum {
  DISENIO(1L, "DISEÑO"),
  PENDIENTE_RECEPCION(2L, "PENDIENTE DE RECEPCIÓN"),
  RECEPCION_PARCIAL(3L, "RECEPCIÓN PARCIAL"),
  RECEPCION_TOTAL(4L, "RECEPCIÓN TOTAL"),
  ANULADA(5L, "ANULADA");

  private final Long id;
  private final String nombre;

  ProveedoresOrdenesCompraEstadosEnum(Long id, String nombre) {
    this.id = id;
    this.nombre = nombre;
  }

  public ProveedoresOrdenesCompraEstadosDto convertToDto() {
    return ProveedoresOrdenesCompraEstadosDto.builder().id(id).nombreEstado(nombre).build();
  }
}

/*
1, 'DISEÑO';
2, 'PENDIENTE DE RECEPCIÓN';
3, 'RECEPCIÓN PARCIAL';
4, 'RECEPCIÓN TOTAL';
5, 'ANULADA';
 */
