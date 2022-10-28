package ar.com.gtsoftware.enums;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;

/** Holds the information of the available Caches in the system. */
@Getter
public enum Caches {
  NONCE("nonce"),
  OFFERS("offers"),
  SESSIONS("sessions");

  private final String cacheName;

  Caches(String cacheName) {
    this.cacheName = cacheName;
  }

  public static List<String> getNames() {
    return Arrays.stream(Caches.values()).map(Caches::getCacheName).toList();
  }
}
