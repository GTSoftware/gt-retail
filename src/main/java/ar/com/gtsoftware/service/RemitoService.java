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

import ar.com.gtsoftware.dto.ProductoMovimiento;
import ar.com.gtsoftware.dto.domain.RemitoDto;
import ar.com.gtsoftware.search.RemitoDetalleSearchFilter;
import ar.com.gtsoftware.search.RemitoSearchFilter;
import java.util.List;

public interface RemitoService extends EntityService<RemitoDto, RemitoSearchFilter> {

  Long guardarRemito(RemitoDto remitoDto);

  List<ProductoMovimiento> getMovimientosProducto(RemitoDetalleSearchFilter sf);
}
