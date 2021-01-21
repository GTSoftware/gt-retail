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

import ar.com.gtsoftware.dto.domain.FiscalPuntosVentaDto;
import ar.com.gtsoftware.service.exceptions.ServiceException;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;

public interface FacturacionVentasService {
  /**
   * Registra la factura fiscalmente en el libro de IVA ventas para la venta en el período fiscal y
   * con la fecha de factura pasados como parámetro
   *
   * @throws ServiceException
   */
  void registrarFacturaVenta(
      @NotNull Long idComprobante,
      @NotNull FiscalPuntosVentaDto puntoVentaComprobanteDto,
      long numeroComprobante,
      LocalDateTime fechaFactura)
      throws ServiceException;

  /**
   * Devuelve el próximo número de factura a utilizar para el punto de venta y letra pasados como
   * parámetro
   *
   * @param letra
   * @param puntoVenta
   * @return número de factura disponible
   */
  long obtenerProximoNumeroFactura(String letra, int puntoVenta);

  /**
   * Anula la factura asociada a la venta pasada como parámetro
   *
   * @param idComprobante
   * @throws ServiceException
   */
  void anularFactura(@NotNull Long idComprobante) throws ServiceException;

  String obtenerCodigoBarrasFE(@NotNull Long idComprobante);
}
