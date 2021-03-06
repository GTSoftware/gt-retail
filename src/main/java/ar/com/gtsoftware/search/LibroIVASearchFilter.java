/*
 * Copyright 2018 GT Software.
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
package ar.com.gtsoftware.search;

import java.time.LocalDateTime;
import java.util.Objects;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LibroIVASearchFilter extends AbstractSearchFilter {

  private Long idPeriodo;
  private LocalDateTime fechaDesde;
  private LocalDateTime fechaHasta;
  private Boolean anuladas;

  @Override
  public boolean hasFilter() {
    return Objects.nonNull(idPeriodo) || hasFechasDesdeHasta() || Objects.nonNull(anuladas);
  }

  public boolean hasFechasDesdeHasta() {
    return (Objects.nonNull(fechaDesde) && Objects.nonNull(fechaHasta))
        && (fechaDesde.compareTo(fechaHasta) <= 0);
  }
}
