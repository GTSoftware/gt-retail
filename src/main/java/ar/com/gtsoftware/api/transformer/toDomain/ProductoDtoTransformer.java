package ar.com.gtsoftware.api.transformer.toDomain;

import ar.com.gtsoftware.api.request.products.CreateOrUpdateProductRequest;
import ar.com.gtsoftware.api.request.products.PricePercent;
import ar.com.gtsoftware.api.request.products.SalePrice;
import ar.com.gtsoftware.api.transformer.Transformer;
import ar.com.gtsoftware.dto.domain.FiscalAlicuotasIvaDto;
import ar.com.gtsoftware.dto.domain.PersonasDto;
import ar.com.gtsoftware.dto.domain.ProductosDto;
import ar.com.gtsoftware.dto.domain.ProductosListasPreciosDto;
import ar.com.gtsoftware.dto.domain.ProductosMarcasDto;
import ar.com.gtsoftware.dto.domain.ProductosPorcentajesDto;
import ar.com.gtsoftware.dto.domain.ProductosPreciosDto;
import ar.com.gtsoftware.dto.domain.ProductosRubrosDto;
import ar.com.gtsoftware.dto.domain.ProductosSubRubrosDto;
import ar.com.gtsoftware.dto.domain.ProductosTiposPorcentajesDto;
import ar.com.gtsoftware.dto.domain.ProductosTiposProveeduriaDto;
import ar.com.gtsoftware.dto.domain.ProductosTiposUnidadesDto;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductoDtoTransformer
    implements Transformer<CreateOrUpdateProductRequest, ProductosDto> {

  public ProductosDto transformFromExisting(
      CreateOrUpdateProductRequest from, ProductosDto existing) {
    final ProductosDto productosDto = transform(from);

    productosDto.setFechaAlta(existing.getFechaAlta());

    return productosDto;
  }

  @Override
  public ProductosDto transform(CreateOrUpdateProductRequest from) {
    Objects.requireNonNull(from);

    return ProductosDto.builder()
        .id(from.getProductId())
        .activo(from.isActive())
        .codigoFabricante(from.getFactoryCode())
        .codigoPropio(from.getCode())
        .costoAdquisicionNeto(from.getGrossCost())
        .costoFinal(from.getNetCost())
        .descripcion(from.getDescription())
        .observaciones(from.getObservations())
        .stockMinimo(from.getMinimumStock())
        .unidadesCompraUnidadesVenta(from.getPurchaseUnitsToSaleUnitEquivalence())
        .ubicacion(from.getLocation())
        .idAlicuotaIva(transformAlicuotaIva(from.getFiscalTaxRateId()))
        .idProveedorHabitual(transformProveedor(from.getRegularSupplierId()))
        .idMarca(transformMarca(from.getBrandId()))
        .idRubro(transformRubro(from.getCategoryId()))
        .idSubRubro(transformSubRubro(from.getSubCategoryId()))
        .idTipoProveeduria(transformTipoProveeduria(from.getSupplyTypeId()))
        .idTipoUnidadCompra(transformTipoUnidad(from.getPurchaseUnitTypeId()))
        .idTipoUnidadVenta(transformTipoUnidad(from.getSaleUnitTypeId()))
        .porcentajes(transformPorcentajes(from.getPercentages()))
        .precios(transformPrecios(from.getSalePrices()))
        .version(from.getVersion())
        .build();
  }

  private List<ProductosPreciosDto> transformPrecios(List<SalePrice> salePrices) {
    List<ProductosPreciosDto> preciosDto = new LinkedList<>();
    for (SalePrice salePrice : salePrices) {
      preciosDto.add(
          ProductosPreciosDto.builder()
              .id(salePrice.getSalePriceId())
              .precio(salePrice.getFinalPrice())
              .neto(salePrice.getNetPrice())
              .utilidad(salePrice.getUtility())
              .version(salePrice.getVersion())
              .idListaPrecios(
                  ProductosListasPreciosDto.builder().id(salePrice.getPriceListId()).build())
              .build());
    }
    return preciosDto;
  }

  private List<ProductosPorcentajesDto> transformPorcentajes(List<PricePercent> percentages) {
    List<ProductosPorcentajesDto> porcentajesDto = new LinkedList<>();
    for (PricePercent percentage : percentages) {
      porcentajesDto.add(
          ProductosPorcentajesDto.builder()
              .id(percentage.getProductPricePercentId())
              .idTipoPorcentaje(
                  ProductosTiposPorcentajesDto.builder().id(percentage.getPercentTypeId()).build())
              .version(percentage.getVersion())
              .valor(percentage.getRate())
              .build());
    }
    return porcentajesDto;
  }

  private ProductosTiposUnidadesDto transformTipoUnidad(Long unitTypeId) {
    return ProductosTiposUnidadesDto.builder().id(unitTypeId).build();
  }

  private ProductosTiposProveeduriaDto transformTipoProveeduria(Long supplyTypeId) {
    return ProductosTiposProveeduriaDto.builder().id(supplyTypeId).build();
  }

  private ProductosSubRubrosDto transformSubRubro(Long subCategoryId) {
    return ProductosSubRubrosDto.builder().id(subCategoryId).build();
  }

  private ProductosRubrosDto transformRubro(Long categoryId) {
    return ProductosRubrosDto.builder().id(categoryId).build();
  }

  private ProductosMarcasDto transformMarca(Long brandId) {
    return ProductosMarcasDto.builder().id(brandId).build();
  }

  private PersonasDto transformProveedor(Long regularSupplierId) {
    return PersonasDto.builder().id(regularSupplierId).build();
  }

  private FiscalAlicuotasIvaDto transformAlicuotaIva(Long fiscalTaxRateId) {
    return FiscalAlicuotasIvaDto.builder().id(fiscalTaxRateId).build();
  }

  @Override
  public List<ProductosDto> transform(List<CreateOrUpdateProductRequest> from) {
    Objects.requireNonNull(from);

    List<ProductosDto> products = new LinkedList<>();
    for (CreateOrUpdateProductRequest productosDto : from) {
      products.add(transform(productosDto));
    }

    return products;
  }
}
