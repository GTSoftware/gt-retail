/*
 * Copyright 2019 GT Software.
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

import ar.com.gtsoftware.dto.domain.NegocioTiposComprobanteDto;
import java.math.BigDecimal;
import lombok.Getter;

@Getter
public enum NegocioTiposComprobanteEnum {
  FACTURA(1L, "FACTURA", 1),
  NOTA_DE_CREDITO(2L, "NOTA DE CREDITO", -1),
  NOTA_DE_DEBITO(3L, "NOTA DE DEBITO", 1),
  PRESUPUESTO(4L, "PRESUPUESTO", 1);

  private final Long id;
  private final String nombre;
  private final int signo;

  NegocioTiposComprobanteEnum(Long id, String nombre, int signo) {
    this.id = id;
    this.nombre = nombre;
    this.signo = signo;
  }

  public NegocioTiposComprobanteDto convertToDto() {
    return NegocioTiposComprobanteDto.builder()
        .id(id)
        .activo(true)
        .nombreComprobante(nombre)
        .signo(BigDecimal.valueOf(signo))
        .build();
  }
}

/*
  id_negocio_tipo_comprobante | nombre_comprobante | signo | activo | version
-----------------------------+--------------------+-------+--------+---------
                           1 | FACTURA            |     1 | t      |       0
                           2 | NOTA DE CREDITO    |    -1 | t      |       0
                           3 | NOTA DE DEBITO     |     1 | t      |       0
                           4 | PRESUPUESTO        |     1 | t      |       0
 */
