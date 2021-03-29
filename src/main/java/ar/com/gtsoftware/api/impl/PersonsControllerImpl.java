package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.PersonsController;
import ar.com.gtsoftware.api.request.PaginatedSearchRequest;
import ar.com.gtsoftware.api.response.PaginatedResponse;
import ar.com.gtsoftware.api.response.PersonSearchResult;
import ar.com.gtsoftware.api.transformer.fromDomain.PersonSearchResultTransformer;
import ar.com.gtsoftware.dto.domain.PersonasDto;
import ar.com.gtsoftware.search.PersonasSearchFilter;
import ar.com.gtsoftware.service.PersonasService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PersonsControllerImpl implements PersonsController {

  private final PersonasService personasService;
  private final PersonSearchResultTransformer searchResultTransformer;

  @Override
  public PaginatedResponse<PersonSearchResult> findBySearchFilter(
      @Valid PaginatedSearchRequest<PersonasSearchFilter> searchRequest) {
    final PersonasSearchFilter searchFilter = searchRequest.getSearchFilter();
    final int count = personasService.countBySearchFilter(searchFilter);

    final PaginatedResponse<PersonSearchResult> response =
        PaginatedResponse.<PersonSearchResult>builder().totalResults(count).build();

    if (count > 0) {
      final List<PersonasDto> personasDtoList =
          personasService.findBySearchFilter(
              searchFilter, searchRequest.getFirstResult(), searchRequest.getMaxResults());
      response.setData(searchResultTransformer.transform(personasDtoList));
    }

    return response;
  }
}
