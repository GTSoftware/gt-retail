package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.request.PaginatedSearchRequest;
import ar.com.gtsoftware.api.response.PaginatedResponse;
import ar.com.gtsoftware.api.response.PersonSearchResult;
import ar.com.gtsoftware.search.PersonasSearchFilter;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface PersonsController {

  @PostMapping(path = "/persons/search")
  PaginatedResponse<PersonSearchResult> findBySearchFilter(
      @Valid @RequestBody PaginatedSearchRequest<PersonasSearchFilter> searchRequest);
}
