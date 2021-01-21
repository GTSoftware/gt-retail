package ar.com.gtsoftware.configuration;

import java.time.Clock;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClockConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public Clock getSystemDefaultZoneClock() {
    return Clock.systemDefaultZone();
  }
}
