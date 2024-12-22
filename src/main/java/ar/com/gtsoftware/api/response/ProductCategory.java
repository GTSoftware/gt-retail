package ar.com.gtsoftware.api.response;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductCategory {

  private Long categoryId;
  @NotNull private String categoryName;
  private String displayName;
  private Integer version;
}
