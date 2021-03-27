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
package ar.com.gtsoftware.service.afip;

import ar.com.gtsoftware.dao.AFIPAuthServicesFacade;
import ar.com.gtsoftware.dto.fiscal.AuthTicket;
import ar.com.gtsoftware.dto.fiscal.CAEResponse;
import ar.com.gtsoftware.entity.AFIPAuthServices;
import ar.com.gtsoftware.entity.FiscalLibroIvaVentas;
import ar.com.gtsoftware.entity.FiscalPuntosVenta;
import ar.com.gtsoftware.entity.FiscalTiposComprobante;
import ar.com.gtsoftware.search.AFIPAuthServicesSearchFilter;
import ar.com.gtsoftware.utils.BusinessDateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AfipService {

  public static final String FACTURA_ELECTRONICA_SERVICE = "wsfe";
  private final AFIPAuthServicesFacade aFIPAuthServicesFacade;
  private final LoginClient loginClient;
  private final ElectronicInvoiceClient electronicInvoiceClient;
  private final BusinessDateUtils dateUtils;

  @Transactional
  public int obtenerUltimoComprobanteAutorizado(
      FiscalPuntosVenta puntoVenta, FiscalTiposComprobante tipoComprobante) {
    final AFIPAuthServices auth = obtenerLoginTicket(FACTURA_ELECTRONICA_SERVICE);

    return electronicInvoiceClient.getLastAuthorizedInvoiceNumber(
        auth,
        puntoVenta.getNroPuntoVenta(),
        Integer.parseInt(tipoComprobante.getCodigoTipoComprobante()));
  }

  @Transactional
  public void autorizarComprobante(FiscalLibroIvaVentas registro) {
    final AFIPAuthServices auth = obtenerLoginTicket(FACTURA_ELECTRONICA_SERVICE);

    CAEResponse caeDto = electronicInvoiceClient.requestElectronicAuthorization(auth, registro);
    registro.setCae(caeDto.getCae());
    registro.setFechaVencimientoCae(caeDto.getFechaVencimientoCae());
  }

  private AFIPAuthServices obtenerLoginTicket(String service) {

    AFIPAuthServices loginTicket = aFIPAuthServicesFacade.find(service);
    if (loginTicket == null) {
      loginTicket = new AFIPAuthServices();
    } else if (loginTicket.getFechaExpiracion().isAfter(dateUtils.getCurrentDateTime())) {
      return loginTicket;
    }
    loginTicket.setNombreServicio(service);

    AuthTicket ticketDto = loginClient.login(service);
    loginTicket.setFechaExpiracion(ticketDto.getExpirationDate());
    loginTicket.setSign(ticketDto.getSign());
    loginTicket.setToken(ticketDto.getToken());
    aFIPAuthServicesFacade.createOrEdit(loginTicket);

    return loginTicket;
  }

  public boolean isAuthenticated(String service) {
    AFIPAuthServicesSearchFilter asf = new AFIPAuthServicesSearchFilter(service, Boolean.TRUE);
    AFIPAuthServices loginTicket = aFIPAuthServicesFacade.findFirstBySearchFilter(asf);
    return loginTicket != null;
  }
}
