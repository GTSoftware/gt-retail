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

import static ar.com.gtsoftware.rules.helper.RuleConditionsTransformer.transformConditions;

import ar.com.gtsoftware.api.PromotionCartItem;
import ar.com.gtsoftware.rules.CondicionIlegalException;
import ar.com.gtsoftware.rules.OfertaDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.api.RulesEngineParameters;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.mvel.MVELRule;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class OfertasHelper {

  private final OfertasFinder ofertasFinder;
  private final CacheManager cacheManager;

  public void ejecutarReglasOferta(PromotionCartItem promotionCartItem) {
    if (!ofertasFinder.existsActiveOffers()) {
      return;
    }

    Facts facts = new Facts();
    facts.put("promotionCartItem", promotionCartItem);
    facts.put("linea", promotionCartItem.getLinea());

    RulesEngineParameters params = new RulesEngineParameters();
    params.setSkipOnFirstAppliedRule(true);

    RulesEngine engine = new DefaultRulesEngine(params);
    engine.fire(loadOffersFromCache(), facts);
  }

  private List<Rule> getOfferRules(List<OfertaDto> ofertasList) {
    List<Rule> rules = new ArrayList<>(ofertasList.size());
    for (OfertaDto ofertaDto : ofertasList) {
      try {
        rules.add(
            new MVELRule()
                .name(String.format("%d - %s", ofertaDto.getId(), ofertaDto.getTextoOferta()))
                .when(transformConditions(ofertaDto.getCondiciones()))
                .then(
                    "promotionCartItem.applyDiscount(ar.com.gtsoftware.rules.TipoAccion."
                        + ofertaDto.getTipoAccion()
                        + ", "
                        + ofertaDto.getDescuento().toPlainString()
                        + ", '"
                        + ofertaDto.getTextoOferta()
                        + "');"));
      } catch (CondicionIlegalException e) {
        //
      }
    }

    return rules;
  }

  // TODO this is here until the add/edit offers is done
  @Scheduled(fixedDelay = 30, timeUnit = TimeUnit.MINUTES)
  public void invalidateCache() {
    log.debug("Evicting offers cache...");
    cacheManager.getCache("offers").invalidate();
  }

  @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.MINUTES)
  @Transactional
  public Rules loadOffersFromCache() {
    if (!ofertasFinder.existsActiveOffers()) {
      return null;
    }

    final Cache offersCache = cacheManager.getCache("offers");
    Rules cachedRules = offersCache.get("rules", Rules.class);
    if (Objects.nonNull(cachedRules)) {
      return cachedRules;
    }

    final List<OfertaDto> ofertasList = ofertasFinder.findOfertas();
    log.debug("Loading {} offers into cache...", ofertasList.size());

    List<Rule> offerRules = getOfferRules(ofertasList);
    Rules rules = new Rules();
    for (Rule rule : offerRules) {
      rules.register(rule);
    }
    offersCache.put("rules", rules);

    return rules;
  }
}
