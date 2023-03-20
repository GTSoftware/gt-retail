package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.PersonsController;
import ar.com.gtsoftware.api.request.PaginatedSearchRequest;
import ar.com.gtsoftware.api.response.PaginatedResponse;
import ar.com.gtsoftware.api.response.PaginatedResponseBuilder;
import ar.com.gtsoftware.api.response.PersonSearchResult;
import ar.com.gtsoftware.api.transformer.fromDomain.PersonSearchResultTransformer;
import ar.com.gtsoftware.search.PersonasSearchFilter;
import ar.com.gtsoftware.service.PersonasService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PersonsControllerImpl implements PersonsController {

  private final PersonasService personasService;
  private final PersonSearchResultTransformer searchResultTransformer;
  private final PaginatedResponseBuilder responseBuilder;

  @Override
  public PaginatedResponse<PersonSearchResult> findBySearchFilter(
      @Valid PaginatedSearchRequest<PersonasSearchFilter> searchRequest) {

    return responseBuilder.build(personasService, searchRequest, searchResultTransformer);
  }
}
