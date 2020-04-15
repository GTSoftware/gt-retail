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
import ar.com.gtsoftware.domain.AFIPAuthServices;
import ar.com.gtsoftware.dto.fiscal.AuthTicket;
import ar.com.gtsoftware.search.AFIPAuthServicesSearchFilter;
import ar.com.gtsoftware.service.ParametrosService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

import static ar.com.gtsoftware.enums.Parametros.*;


@Service
@RequiredArgsConstructor
public class WSAAService {

    private final AFIPAuthServicesFacade aFIPAuthServicesFacade;
    private final ParametrosService parametrosFacade;

    public boolean isAuthenticated(String service) {
        AFIPAuthServicesSearchFilter asf = new AFIPAuthServicesSearchFilter(service, Boolean.TRUE);
        AFIPAuthServices loginTicket = aFIPAuthServicesFacade.findFirstBySearchFilter(asf);
        return loginTicket != null;
    }

    public AFIPAuthServices obtenerLoginTicket(String service) {

        AFIPAuthServices loginTicket = aFIPAuthServicesFacade.find(service);
        if (loginTicket == null) {
            loginTicket = new AFIPAuthServices();
        } else if (loginTicket.getFechaExpiracion().after(new Date())) {
            return loginTicket;
        }
        loginTicket.setNombreServicio(service);
        String endpoint = parametrosFacade.getStringParam(AFIP_WSFE_ENDPOINT);
        String certPath = parametrosFacade.getStringParam(AFIP_CERT_PATH);
        String certPassword = parametrosFacade.getStringParam(AFIP_CERT_PASSWORD);
        String dstDN = parametrosFacade.getStringParam(AFIP_DN_PARAM);

        AuthTicket ticketDto = WSAAClient.performAuthentication(endpoint, certPath, certPassword, dstDN, service);
        loginTicket.setFechaExpiracion(ticketDto.getExpirationDate());
        loginTicket.setSign(ticketDto.getSign());
        loginTicket.setToken(ticketDto.getToken());
        aFIPAuthServicesFacade.createOrEdit(loginTicket);

        return loginTicket;
    }
}
