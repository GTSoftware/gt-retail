package ar.com.gtsoftware.rules.helper;

import ar.com.gtsoftware.rules.OfertaDto;
import ar.com.gtsoftware.search.OfertasSearchFilter;
import ar.com.gtsoftware.service.OfertasService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OfertasFinder {

    private final OfertasService ofertasService;

    @Cacheable(cacheNames = "offers")
    public List<OfertaDto> findOfertas() {
        final OfertasSearchFilter filter = OfertasSearchFilter.builder().activas(true).build();

        return ofertasService.findAllBySearchFilter(filter);
    }

}
