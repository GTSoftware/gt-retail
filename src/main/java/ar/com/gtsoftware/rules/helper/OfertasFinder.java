package ar.com.gtsoftware.rules.helper;

import ar.com.gtsoftware.rules.OfertaDto;
import ar.com.gtsoftware.search.OfertasSearchFilter;
import ar.com.gtsoftware.service.OfertasService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OfertasFinder {

  private final OfertasService ofertasService;

  public List<OfertaDto> findOfertas() {
    final OfertasSearchFilter filter = OfertasSearchFilter.builder().activas(true).build();

    return ofertasService.findAllBySearchFilter(filter);
  }

  public boolean existsActiveOffers() {
    final OfertasSearchFilter filter = OfertasSearchFilter.builder().activas(true).build();

    return ofertasService.countBySearchFilter(filter) > 0;
  }
}
