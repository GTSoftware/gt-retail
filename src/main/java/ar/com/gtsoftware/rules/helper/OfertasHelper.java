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
import ar.com.gtsoftware.dto.domain.ComprobantesLineasDto;
import ar.com.gtsoftware.dto.domain.ProductosDto;
import ar.com.gtsoftware.rules.Campo;
import ar.com.gtsoftware.rules.Condicion;
import ar.com.gtsoftware.rules.OfertaDto;
import ar.com.gtsoftware.rules.Operacion;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class OfertasHelper {

  private static final String OFFERS_CACHE_KEY = "offersList";

  private final OfertasFinder ofertasFinder;
  private final CacheManager cacheManager;

  public void ejecutarReglasOferta(PromotionCartItem promotionCartItem) {
    if (!ofertasFinder.existsActiveOffers()) {
      return;
    }

    List<OfertaDto> ofertas = loadOffersFromCache();
    if (ofertas == null || ofertas.isEmpty()) {
      return;
    }

    // Apply first matching offer (skip on first applied rule behavior)
    for (OfertaDto oferta : ofertas) {
      if (matches(oferta, promotionCartItem)) {
        promotionCartItem.applyDiscount(
            oferta.getTipoAccion(), oferta.getDescuento(), oferta.getTextoOferta());
        break;
      }
    }
  }

  private boolean matches(OfertaDto oferta, PromotionCartItem item) {
    List<Condicion> condiciones = oferta.getCondiciones();
    if (condiciones == null || condiciones.isEmpty()) {
      return false;
    }
    for (Condicion c : condiciones) {
      if (!evaluateCondition(c, item.getLinea())) {
        return false; // all conditions must hold (AND)
      }
    }
    return true;
  }

  private boolean evaluateCondition(Condicion condicion, ComprobantesLineasDto linea) {
    Campo campo = condicion.getCampo();
    Operacion op = condicion.getOperacion();
    String valorStr = condicion.getValor();

    Object left = extractFieldValue(campo, linea);
    if (left == null) {
      return false;
    }

    try {
      switch (op) {
        case CONTIENE:
          String s = String.valueOf(left);
          return s != null && s.toUpperCase().contains(valorStr.toUpperCase());
        case MULTIPLO:
          {
            BigDecimal l = toBigDecimal(left);
            BigDecimal r = new BigDecimal(valorStr);
            if (r.compareTo(BigDecimal.ZERO) == 0) return false;
            return l.remainder(r).compareTo(BigDecimal.ZERO) == 0;
          }
        case IGUAL:
          return compare(left, valorStr) == 0;
        case DISTINTO:
          return compare(left, valorStr) != 0;
        case MAYOR:
          return compare(left, valorStr) > 0;
        case MAYOR_IGUAL:
          return compare(left, valorStr) >= 0;
        case MENOR:
          return compare(left, valorStr) < 0;
        case MENOR_IGUAL:
          return compare(left, valorStr) <= 0;
        default:
          return false;
      }
    } catch (RuntimeException ex) {
      log.debug("Error evaluating condition {} on {}: {}", condicion, linea, ex.getMessage());
      return false;
    }
  }

  private int compare(Object left, String rightAsString) {
    if (left instanceof BigDecimal l) {
      return l.compareTo(new BigDecimal(rightAsString));
    }
    if (left instanceof Long l) {
      return l.compareTo(Long.valueOf(rightAsString));
    }
    if (left instanceof Integer l) {
      return l.compareTo(Integer.valueOf(rightAsString));
    }
    if (left instanceof String l) {
      return l.compareTo(rightAsString);
    }
    if (left instanceof java.util.Date l) {
      return l.compareTo(new java.util.Date(Long.parseLong(rightAsString)));
    }
    throw new IllegalArgumentException("Unsupported type: " + left.getClass());
  }

  private BigDecimal toBigDecimal(Object o) {
    if (o instanceof BigDecimal b) return b;
    if (o instanceof Long l) return new BigDecimal(l);
    if (o instanceof Integer i) return new BigDecimal(i);
    if (o instanceof String s) return new BigDecimal(s);
    throw new IllegalArgumentException("Cannot convert to BigDecimal: " + o);
  }

  private Object extractFieldValue(Campo campo, ComprobantesLineasDto linea) {
    ProductosDto prod = linea.getIdProducto();
    switch (campo) {
      case DESCRIPCION:
        return linea.getDescripcion();
      case ID_PRODUCTO:
        return prod != null ? prod.getId() : null;
      case ID_PROVEEDOR:
        return prod != null && prod.getIdProveedorHabitual() != null
            ? prod.getIdProveedorHabitual().getId()
            : null;
      case NOMBRE_RUBRO:
        return prod != null && prod.getIdRubro() != null
            ? prod.getIdRubro().getNombreRubro()
            : null;
      case ID_RUBRO:
        return prod != null && prod.getIdRubro() != null ? prod.getIdRubro().getId() : null;
      case NOMBRE_SUB_RUBRO:
        return prod != null && prod.getIdSubRubro() != null
            ? prod.getIdSubRubro().getNombreSubRubro()
            : null;
      case ID_SUB_RUBRO:
        return prod != null && prod.getIdSubRubro() != null ? prod.getIdSubRubro().getId() : null;
      case ID_MARCA:
        return prod != null && prod.getIdMarca() != null ? prod.getIdMarca().getId() : null;
      case NOMBRE_MARCA:
        return prod != null && prod.getIdMarca() != null
            ? prod.getIdMarca().getNombreMarca()
            : null;
      case CANTIDAD:
        return linea.getCantidad();
      case PRECIO_UNITARIO:
        return linea.getPrecioUnitario();
      default:
        return null;
    }
  }

  // TODO this is here until the add/edit offers is done
  @Scheduled(fixedDelay = 30, timeUnit = TimeUnit.MINUTES)
  public void invalidateCache() {
    log.debug("Evicting offers cache...");
    cacheManager.getCache("offers").invalidate();
  }

  @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.MINUTES)
  @Transactional(readOnly = true)
  public List<OfertaDto> loadOffersFromCache() {
    if (!ofertasFinder.existsActiveOffers()) {
      return null;
    }

    final Cache offersCache = cacheManager.getCache("offers");
    List<OfertaDto> cached = offersCache.get(OFFERS_CACHE_KEY, List.class);
    if (cached != null) {
      return cached;
    }

    final List<OfertaDto> ofertasList = ofertasFinder.findOfertas();
    log.debug("Loading {} offers into cache...", ofertasList.size());

    offersCache.put(OFFERS_CACHE_KEY, ofertasList);
    return ofertasList;
  }
}
