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

import ar.com.gtsoftware.dao.ComprobantesFacade;
import ar.com.gtsoftware.domain.Comprobantes;
import ar.com.gtsoftware.dto.domain.ComprobantesDto;
import ar.com.gtsoftware.mappers.ComprobantesMapper;
import ar.com.gtsoftware.mappers.PersonasMapper;
import ar.com.gtsoftware.search.ComprobantesSearchFilter;
import ar.com.gtsoftware.service.BaseEntityService;
import ar.com.gtsoftware.service.ComprobantesService;
import ar.com.gtsoftware.service.PersonasCuentaCorrienteService;
import ar.com.gtsoftware.service.exceptions.ServiceException;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ComprobantesServiceImpl
    extends BaseEntityService<ComprobantesDto, ComprobantesSearchFilter, Comprobantes>
    implements ComprobantesService {

  private final ComprobantesFacade facade;
  private final PersonasCuentaCorrienteService cuentaCorrienteBean;

  private final ComprobantesMapper mapper;

  private final PersonasMapper personasMapper;

  @Override
  protected ComprobantesFacade getFacade() {
    return facade;
  }

  @Override
  protected ComprobantesMapper getMapper() {
    return mapper;
  }

  @Override
  public BigDecimal calcularTotalVentas(ComprobantesSearchFilter sf) {
    return facade.calcularTotalVentasBySearchFilter(sf);
  }

  @Override
  public void anularVenta(Long idComprobante) throws ServiceException {
    Comprobantes venta = facade.find(idComprobante);
    if (venta == null) {
      throw new ServiceException("Comprobante inexistente");
    }
    if (venta.isAnulada()) {
      throw new ServiceException("El comprobante ya fue anulado!");
    }
    if (venta.getIdRegistro() != null) {
      throw new ServiceException("Comprobante impreso fiscalmente!");
    }
    if (venta.getTotal().subtract(venta.getSaldo()).signum() > 0) {
      throw new ServiceException("Comprobante total o parcialmente cobrado!");
    }
    venta.setAnulada(true);
    cuentaCorrienteBean.registrarMovimientoCuenta(
        venta.getIdPersona(),
        venta.getTotalConSigno(),
        String.format("Anulación comprobante Nro: %d", venta.getId()));
    facade.edit(venta);
  }
}
