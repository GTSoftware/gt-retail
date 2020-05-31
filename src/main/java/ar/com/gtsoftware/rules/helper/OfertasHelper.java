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
import ar.com.gtsoftware.rules.CondicionIlegalException;
import ar.com.gtsoftware.rules.OfertaDto;
import lombok.RequiredArgsConstructor;
import org.jeasy.rules.api.*;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.mvel.MVELRule;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static ar.com.gtsoftware.rules.helper.RuleConditionsTransformer.transformConditions;

@Component
@RequiredArgsConstructor
public class OfertasHelper {

    private final OfertasFinder ofertasFinder;

    public void ejecutarReglasOferta(PromotionCartItem promotionCartItem) {
        final List<OfertaDto> ofertasList = ofertasFinder.findOfertas();

        if (!ofertasList.isEmpty()) {
            List<Rule> offerRules = getOfferRules(ofertasList);

            Facts facts = new Facts();
            facts.put("promotionCartItem", promotionCartItem);
            facts.put("linea", promotionCartItem.getLinea());

            Rules rules = new Rules();
            for (Rule rule : offerRules) {
                rules.register(rule);
            }
            RulesEngineParameters params = new RulesEngineParameters();
            params.setSkipOnFirstAppliedRule(true);

            RulesEngine engine = new DefaultRulesEngine(params);
            engine.fire(rules, facts);
        }
    }


    private List<Rule> getOfferRules(List<OfertaDto> ofertasList) {
        List<Rule> rules = new ArrayList<>(ofertasList.size());
        for (OfertaDto ofertaDto : ofertasList) {
            try {
                rules.add(
                        new MVELRule()
                                .name(String.format("%d - %s", ofertaDto.getId(), ofertaDto.getTextoOferta()))
                                .when(transformConditions(ofertaDto.getCondiciones()))
                                .then("promotionCartItem.applyDiscount(ar.com.gtsoftware.rules.TipoAccion." + ofertaDto.getTipoAccion() + ", " +
                                        ofertaDto.getDescuento().toPlainString() + ", '" +
                                        ofertaDto.getTextoOferta() + "');")
                );
            } catch (CondicionIlegalException e) {
                //
            }
        }

        return rules;
    }
}
