package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.request.PaginatedSearchRequest;
import ar.com.gtsoftware.api.response.PaginatedResponse;
import ar.com.gtsoftware.api.response.PersonSearchResult;
import ar.com.gtsoftware.search.PersonasSearchFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface PersonsController {

    @PostMapping(path = "/persons/search")
    PaginatedResponse<PersonSearchResult> findBySearchFilter(@Valid @RequestBody PaginatedSearchRequest<PersonasSearchFilter> searchRequest);
}
