package ar.com.gtsoftware.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductCategory {

  private Long categoryId;
  private String categoryName;
  private String displayName;
}
