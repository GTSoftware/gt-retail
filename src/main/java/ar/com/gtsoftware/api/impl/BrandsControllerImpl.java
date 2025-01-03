package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.BrandsController;
import ar.com.gtsoftware.api.exception.BrandNotFoundException;
import ar.com.gtsoftware.api.response.ProductBrand;
import ar.com.gtsoftware.api.transformer.fromDomain.BrandsTransformer;
import ar.com.gtsoftware.dto.domain.ProductosMarcasDto;
import ar.com.gtsoftware.search.MarcasSearchFilter;
import ar.com.gtsoftware.service.ProductosMarcasService;
import ar.com.gtsoftware.utils.TextUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BrandsControllerImpl implements BrandsController {

  private final ProductosMarcasService productosMarcasService;
  private final BrandsTransformer brandsTransformer;

  @Override
  @Transactional(readOnly = true)
  public List<ProductBrand> getProductBrands() {
    MarcasSearchFilter sf = new MarcasSearchFilter();
    sf.addSortField("nombreMarca", true);

    return brandsTransformer.transform(productosMarcasService.findAllBySearchFilter(sf));
  }

  @Override
  @Transactional(readOnly = true)
  public ProductBrand getProductBrand(Long brandId) {
    ProductosMarcasDto marcaDto = productosMarcasService.find(brandId);
    if (marcaDto == null) {
      throw new BrandNotFoundException();
    }
    return brandsTransformer.transform(marcaDto);
  }

  @Override
  @Transactional
  public ProductBrand updateBrand(Long brandId, ProductBrand productBrand) {
    ProductosMarcasDto marcaDto = productosMarcasService.find(brandId);
    if (marcaDto == null) {
      throw new BrandNotFoundException();
    }
    marcaDto.setNombreMarca(productBrand.getBrandName());
    marcaDto.setVersion(productBrand.getVersion());

    return brandsTransformer.transform(productosMarcasService.createOrEdit(marcaDto));
  }

  @Override
  @Transactional
  public ProductBrand createBrand(ProductBrand productBrand) {
    ProductosMarcasDto marcaDto = new ProductosMarcasDto();
    marcaDto.setNombreMarca(TextUtils.upperCaseTrim(productBrand.getBrandName()));

    return brandsTransformer.transform(productosMarcasService.createOrEdit(marcaDto));
  }

  @Override
  @Transactional
  public void deleteBrand(Long brandId) {
    ProductosMarcasDto marcaDto = productosMarcasService.find(brandId);
    if (marcaDto == null) {
      throw new BrandNotFoundException();
    }
    productosMarcasService.remove(marcaDto);
  }
}
