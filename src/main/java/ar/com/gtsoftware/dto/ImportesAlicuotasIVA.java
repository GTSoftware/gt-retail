/*
 * Copyright 2014 GT Software.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ar.com.gtsoftware.dto;

import ar.com.gtsoftware.dto.domain.FiscalAlicuotasIvaDto;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

/**
 * Almacena el importeIva y el NetoGravado que se genera por cada alícuota de IVA
 *
 * @author Rodrigo Tato <rotatomel@gmail.com>
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImportesAlicuotasIVA implements Serializable {

  private static final long serialVersionUID = 2L;

  @EqualsAndHashCode.Include private FiscalAlicuotasIvaDto alicuota;
  private BigDecimal importeIva;
  private BigDecimal netoGravado;
}
