package ar.com.gtsoftware.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class ProductCategory {

  private Long categoryId;
  @NotNull
  private String categoryName;
  private String displayName;
  private Integer version;
}
