package ar.com.gtsoftware.api.request;

import ar.com.gtsoftware.search.AbstractSearchFilter;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PaginatedSearchRequest<T extends AbstractSearchFilter> {

    @Min(0)
    private Integer firstResult;
    @Min(1)
    private Integer maxResults;
    @NotNull
    private T searchFilter;
}
