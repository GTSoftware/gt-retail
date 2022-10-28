package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.exception.IdempotenceException;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

/** Handles the tasks needed for avoiding processing duplicated requests. */
@RequiredArgsConstructor
@Component
public class IdempotenceHandler {
  private static final String NONCE = "nonce";
  private final CacheManager cacheManager;

  /**
   * Will verify if the nonce has been used. Will throw <code>IdempotenceException</code> if it has
   * been used.
   *
   * @param nonce
   */
  public void verifyIdempotence(String nonce) {
    final Cache nonceCache = getNonceCache();
    final Optional<String> usedNonce = Optional.ofNullable(nonceCache.get(nonce, String.class));
    if (usedNonce.isPresent()) {
      throw new IdempotenceException("Evento ya registrado con Id: " + usedNonce.get());
    }
  }

  /**
   * Set the <code>nonce</code> as used with the <code>eventId</code>.
   *
   * @param nonce
   * @param eventId
   */
  public void setNonce(String nonce, String eventId) {
    getNonceCache().put(nonce, eventId);
  }

  private Cache getNonceCache() {
    return Objects.requireNonNull(cacheManager.getCache(NONCE), "Nonce cache not configured");
  }
}
