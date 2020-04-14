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

package ar.com.gtsoftware.rules.helper;

import ar.com.gtsoftware.api.PromotionCartItem;
import ar.com.gtsoftware.rules.OfertaDto;
import ar.com.gtsoftware.search.OfertasSearchFilter;
import ar.com.gtsoftware.service.OfertasService;
import lombok.RequiredArgsConstructor;
import org.kie.api.runtime.StatelessKieSession;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OfertasHelper {

    private final OfertasService ofertasService;
    private final DroolsUtility droolsUtility;

    public void ejecutarReglasOferta(PromotionCartItem promotionCartItem) {
        try {
            StatelessKieSession session = initializeSession();
            session.setGlobal("promotionCartItem", promotionCartItem);

            session.execute(promotionCartItem.getLinea());
        } catch (Exception e) {
            throw new RuntimeException("Imposible inicializar el contexto de reglas.", e);
        }
    }

    private StatelessKieSession initializeSession() throws Exception {
        //filter.setIdSucursal(venta.getIdSucursal().getId());
        final OfertasSearchFilter filter = OfertasSearchFilter.builder().activas(true).build();

        List<OfertaDto> ofertasList = ofertasService.findAllBySearchFilter(filter);
        return droolsUtility.loadSession(ofertasList);
    }
}
