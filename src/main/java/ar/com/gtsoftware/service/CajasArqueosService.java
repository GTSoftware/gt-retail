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

package ar.com.gtsoftware.service;

import ar.com.gtsoftware.dto.domain.CajasArqueosDto;
import ar.com.gtsoftware.dto.domain.UsuariosDto;
import ar.com.gtsoftware.search.CajasArqueosSearchFilter;
import java.time.LocalDateTime;

public interface CajasArqueosService
    extends EntityService<CajasArqueosDto, CajasArqueosSearchFilter> {

  /**
   * Realiza el control del arqueo.
   *
   * @param idArqueo
   * @param usuarioControl
   * @param fechaControl
   * @param observaciones
   */
  void controlar(
      Long idArqueo, UsuariosDto usuarioControl, LocalDateTime fechaControl, String observaciones);
}
