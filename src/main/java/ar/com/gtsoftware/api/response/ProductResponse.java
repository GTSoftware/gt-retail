package ar.com.gtsoftware.api.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ProductResponse {
  private Long productId;
  private String code;
  private String description;
  private String observations;
  private LocalDateTime createdDate;
  private LocalDateTime lastUpdatedDate;
  private boolean active;
  private BigDecimal grossCost;
  private BigDecimal netCost;
  // private int annosAmortizacion;
  private BigDecimal purchaseUnitsToSaleUnitEquivalence;
  private ProductUnitType saleUnit;
  private ProductUnitType purchaseUnit;
  private ProductSupplyType supplyType;
  private ProductCategory category;
  private ProductSubCategory subCategory;
  private PersonSearchResult regularSupplier;
  private FiscalTaxRate fiscalTaxRate;
  private ProductBrand brand;
  private List<ProductSalePrice> salePrices;
  private List<ProductPricePercent> percentages;
  private String location;
  private String factoryCode;
  private BigDecimal minimumStock;
  private Integer version;
}
