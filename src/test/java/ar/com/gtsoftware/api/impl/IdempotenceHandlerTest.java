package ar.com.gtsoftware.api.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import ar.com.gtsoftware.api.exception.IdempotenceException;
import ar.com.gtsoftware.enums.Caches;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;

@ExtendWith(MockitoExtension.class)
class IdempotenceHandlerTest {

  @Mock private CacheManager cacheManager;
  private IdempotenceHandler handler;

  @BeforeEach
  void setUp() {
    when(cacheManager.getCache(Caches.NONCE.getCacheName()))
        .thenReturn(new ConcurrentMapCache("test"));
    handler = new IdempotenceHandler(cacheManager);
  }

  @Test
  void shouldNotThrowForNotExistingNonce() {
    handler.verifyIdempotence("test-nonce");
  }

  @Test
  void shouldThrowForNotExistingNonce() {
    handler.setNonce("test-nonce", "test");
    assertThrows(IdempotenceException.class, () -> handler.verifyIdempotence("test-nonce"));
  }

  @Test
  void shouldThrowWhenCacheIsNotConfigured() {
    when(cacheManager.getCache(Caches.NONCE.getCacheName())).thenReturn(null);

    assertThrows(NullPointerException.class, () -> handler.verifyIdempotence("test-nonce"));
    assertThrows(NullPointerException.class, () -> handler.setNonce("test-nonce", "test"));
  }
}
