package ar.com.gtsoftware.api.response;

import ar.com.gtsoftware.api.request.PaginatedSearchRequest;
import ar.com.gtsoftware.api.transformer.Transformer;
import ar.com.gtsoftware.search.AbstractSearchFilter;
import ar.com.gtsoftware.service.EntityService;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PaginatedResponseBuilder {

  public <R, S extends AbstractSearchFilter> PaginatedResponse<R> build(
      EntityService<R, S> service, PaginatedSearchRequest<S> searchRequest) {
    final S searchFilter = searchRequest.getSearchFilter();
    final int totalResults = service.countBySearchFilter(searchFilter);

    if (totalResults > 0) {
      final List<R> results =
          service.findBySearchFilter(
              searchFilter, searchRequest.getFirstResult(), searchRequest.getMaxResults());
      return PaginatedResponse.<R>builder().data(results).totalResults(totalResults).build();
    }

    return PaginatedResponse.<R>builder().totalResults(0).build();
  }

  public <R, T, S extends AbstractSearchFilter> PaginatedResponse<R> build(
      EntityService<T, S> service,
      PaginatedSearchRequest<S> searchRequest,
      Transformer<T, R> transformer) {
    final S searchFilter = searchRequest.getSearchFilter();
    final int totalResults = service.countBySearchFilter(searchFilter);

    if (totalResults > 0) {
      final List<T> results =
          service.findBySearchFilter(
              searchFilter, searchRequest.getFirstResult(), searchRequest.getMaxResults());
      return PaginatedResponse.<R>builder()
          .data(transformer.transform(results))
          .totalResults(totalResults)
          .build();
    }

    return PaginatedResponse.<R>builder().totalResults(0).build();
  }
}
