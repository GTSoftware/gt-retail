package ar.com.gtsoftware.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ClockConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public Clock getSystemDefaultZoneClock() {
        return Clock.systemDefaultZone();
    }
}
