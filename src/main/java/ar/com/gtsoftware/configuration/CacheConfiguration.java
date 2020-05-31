package ar.com.gtsoftware.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableCaching
public class CacheConfiguration {

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("offers");
    }

    //TODO this is here until the add/edit offers is done
    @CacheEvict(allEntries = true, cacheNames = {"offers"})
    @Scheduled(fixedDelay = 30 * 60000)//30 Minutes
    public void cacheEvict() {
    }
}