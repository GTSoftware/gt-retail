package ar.com.gtsoftware.api.request;

import ar.com.gtsoftware.search.AbstractSearchFilter;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginatedSearchRequest<T extends AbstractSearchFilter> {

  @Min(0)
  private Integer firstResult;

  @Min(1)
  private Integer maxResults;

  @NotNull @Valid private T searchFilter;
}
