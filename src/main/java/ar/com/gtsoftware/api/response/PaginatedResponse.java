package ar.com.gtsoftware.api.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaginatedResponse<T> {
  private List<T> data;
  private int totalResults;
}
