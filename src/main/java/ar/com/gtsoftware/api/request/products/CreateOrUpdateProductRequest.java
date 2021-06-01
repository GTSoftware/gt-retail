package ar.com.gtsoftware.api.request.products;

import java.math.BigDecimal;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CreateOrUpdateProductRequest {
  private Long productId;
  @NotNull @NotEmpty private String code;
  @NotNull private String description;
  private String observations;
  private boolean active;
  @NotNull private BigDecimal grossCost;
  @NotNull private BigDecimal netCost;
  // private int annosAmortizacion;
  @NotNull private BigDecimal purchaseUnitsToSaleUnitEquivalence;
  @NotNull private Long saleUnitTypeId;
  @NotNull private Long purchaseUnitTypeId;
  @NotNull private Long supplyTypeId;
  @NotNull private Long categoryId;
  @NotNull private Long subCategoryId;
  @NotNull private Long regularSupplierId;
  @NotNull private Long fiscalTaxRateId;
  @NotNull private Long brandId;
  @NotEmpty @NotNull @Valid private List<SalePrice> salePrices;
  @Valid private List<PricePercent> percentages;
  private String location;
  private String factoryCode;
  @NotNull private BigDecimal minimumStock;
  private Integer version;
}
