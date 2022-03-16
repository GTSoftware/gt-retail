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

package ar.com.gtsoftware.mappers;

import ar.com.gtsoftware.dto.domain.RecibosDetalleDto;
import ar.com.gtsoftware.entity.RecibosDetalle;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {NegocioFormasPagoMapper.class, ComprobantesPagosMapper.class})
public interface RecibosDetalleMapper extends GenericMapper<RecibosDetalle, RecibosDetalleDto> {}
