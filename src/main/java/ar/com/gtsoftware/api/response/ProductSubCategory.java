package ar.com.gtsoftware.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductSubCategory {
    private Long subCategoryId;
    private Long categoryId;
    private String subCategoryName;
    private String displayName;
}
