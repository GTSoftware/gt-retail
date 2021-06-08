package ar.com.gtsoftware.api.transformer.fromDomain;

import ar.com.gtsoftware.api.response.ProductPricePercent;
import ar.com.gtsoftware.api.response.ProductResponse;
import ar.com.gtsoftware.api.response.ProductSalePrice;
import ar.com.gtsoftware.api.transformer.Transformer;
import ar.com.gtsoftware.dto.domain.ProductosDto;
import ar.com.gtsoftware.dto.domain.ProductosPorcentajesDto;
import ar.com.gtsoftware.dto.domain.ProductosPreciosDto;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductResponseTransformer implements Transformer<ProductosDto, ProductResponse> {

  private final BrandsTransformer brandTransformer;
  private final CategoriesTransformer categoryTransformer;
  private final SubCategoriesTransformer subCategoryTransformer;
  private final FiscalTaxRateTransformer taxRateTransformer;
  private final ProductPercentTypeTransformer percentTypeTransformer;
  private final ProductUnitTypeTransformer unitTypeTransformer;
  private final SupplyTypesTransformer supplyTypeTransformer;
  private final PersonSearchResultTransformer personSearchResultTransformer;
  private final PriceListTransformer priceListTransformer;

  @Override
  public ProductResponse transform(ProductosDto from) {
    Objects.requireNonNull(from);

    return ProductResponse.builder()
        .productId(from.getId())
        .active(from.isActivo())
        .code(from.getCodigoPropio())
        .description(from.getDescripcion())
        .factoryCode(from.getCodigoFabricante())
        .createdDate(from.getFechaAlta())
        .lastUpdatedDate(from.getFechaUltimaModificacion())
        .grossCost(from.getCostoAdquisicionNeto())
        .location(from.getUbicacion())
        .minimumStock(from.getStockMinimo())
        .netCost(from.getCostoFinal())
        .observations(from.getObservaciones())
        .version(from.getVersion())
        .brand(brandTransformer.transform(from.getIdMarca()))
        .category(categoryTransformer.transform(from.getIdRubro()))
        .fiscalTaxRate(taxRateTransformer.transform(from.getIdAlicuotaIva()))
        .percentages(transformPricePercent(from.getPorcentajes()))
        .purchaseUnit(unitTypeTransformer.transform(from.getIdTipoUnidadCompra()))
        .saleUnit(unitTypeTransformer.transform(from.getIdTipoUnidadVenta()))
        .purchaseUnitsToSaleUnitEquivalence(from.getUnidadesCompraUnidadesVenta())
        .subCategory(subCategoryTransformer.transform(from.getIdSubRubro()))
        .supplyType(supplyTypeTransformer.transform(from.getIdTipoProveeduria()))
        .regularSupplier(
            Objects.isNull(from.getIdProveedorHabitual())
                ? null
                : personSearchResultTransformer.transform(from.getIdProveedorHabitual()))
        .salePrices(transformSalePrices(from.getPrecios()))
        .build();
  }

  private List<ProductSalePrice> transformSalePrices(List<ProductosPreciosDto> precios) {
    List<ProductSalePrice> prices = new LinkedList<>();
    for (ProductosPreciosDto precio : precios) {
      prices.add(
          ProductSalePrice.builder()
              .salePriceId(precio.getId())
              .finalPrice(precio.getPrecio())
              .utility(precio.getUtilidad())
              .netPrice(precio.getNeto())
              .lastUpdatedDate(precio.getFechaModificacion())
              .priceList(priceListTransformer.transform(precio.getIdListaPrecios()))
              .version(precio.getVersion())
              .build());
    }

    return prices;
  }

  private List<ProductPricePercent> transformPricePercent(
      List<ProductosPorcentajesDto> porcentajes) {
    List<ProductPricePercent> percents = new LinkedList<>();
    for (ProductosPorcentajesDto porcentaje : porcentajes) {
      percents.add(
          ProductPricePercent.builder()
              .rate(porcentaje.getValor())
              .version(porcentaje.getVersion())
              .lastUpdatedDate(porcentaje.getFechaModificacion())
              .productPricePercentId(porcentaje.getId())
              .percentType(percentTypeTransformer.transform(porcentaje.getIdTipoPorcentaje()))
              .build());
    }

    return percents;
  }

  @Override
  public List<ProductResponse> transform(List<ProductosDto> from) {
    Objects.requireNonNull(from);

    List<ProductResponse> products = new LinkedList<>();
    for (ProductosDto productosDto : from) {
      products.add(transform(productosDto));
    }

    return products;
  }
}
