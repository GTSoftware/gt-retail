/*
 * Copyright 2016 GT Software.
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
package ar.com.gtsoftware.service;

import ar.com.gtsoftware.dto.PagoValorDTO;
import ar.com.gtsoftware.dto.domain.CajasDto;
import ar.com.gtsoftware.dto.domain.RecibosDto;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Capa de servicio para cobros de comprobantes
 *
 * @author Rodrigo M. Tato Rothamel mailto:rotatomel@gmail.com
 */

public interface CobranzaService {

    /**
     * Cobra totalmente uno o más comprobantes
     *
     * @param caja
     * @param pagos
     * @return
     */
    RecibosDto cobrarComprobantes(@NotNull CajasDto caja, List<PagoValorDTO> pagos);

}
