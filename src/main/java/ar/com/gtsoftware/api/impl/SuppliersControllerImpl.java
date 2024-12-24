package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.SuppliersController;
import ar.com.gtsoftware.api.exception.CustomerNotFoundException;
import ar.com.gtsoftware.api.request.CreateOrUpdateCustomerRequest;
import ar.com.gtsoftware.api.response.CustomerResponse;
import ar.com.gtsoftware.api.transformer.fromDomain.CustomerTransformer;
import ar.com.gtsoftware.api.transformer.toDomain.PersonaDtoTransformer;
import ar.com.gtsoftware.auth.Roles;
import ar.com.gtsoftware.dto.domain.PersonasDto;
import ar.com.gtsoftware.search.PersonasSearchFilter;
import ar.com.gtsoftware.service.PersonasService;
import ar.com.gtsoftware.service.ProveedoresService;
import ar.com.gtsoftware.utils.SecurityUtils;
import java.util.Objects;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SuppliersControllerImpl implements SuppliersController {

  private final ProveedoresService proveedoresService;
  private final PersonasService personasService;
  private final CustomerTransformer customerTransformer;
  private final PersonaDtoTransformer personaDtoTransformer;
  private final SecurityUtils securityUtils;

  @Override
  @Transactional
  public CustomerResponse addNewSupplier(@Valid CreateOrUpdateCustomerRequest newSupplier) {
    securityUtils.checkUserRole(Roles.ADMINISTRADORES);
    PersonasDto proveedor = personaDtoTransformer.transformNewCustomer(newSupplier);

    return customerTransformer.transformCustomer(proveedoresService.guardarProveedor(proveedor));
  }

  @Override
  public CustomerResponse getSupplier(Long identificationTypeId, Long identificationNumber) {
    securityUtils.checkUserRole(Roles.ADMINISTRADORES);

    final PersonasSearchFilter psf =
        PersonasSearchFilter.builder()
            .idTipoDocumento(identificationTypeId)
            .documento(identificationNumber.toString())
            .activo(true)
            .build();
    final PersonasDto personaDto = personasService.findFirstBySearchFilter(psf);

    if (personaDto == null) {
      throw new CustomerNotFoundException();
    }

    return customerTransformer.transformCustomer(personaDto);
  }

  @Override
  public CustomerResponse getSupplierById(Long supplierId) {
    securityUtils.checkUserRole(Roles.ADMINISTRADORES);

    final PersonasDto personaDto = personasService.find(supplierId);

    if (personaDto == null) {
      throw new CustomerNotFoundException();
    }

    return customerTransformer.transformCustomer(personaDto);
  }

  @Override
  @Transactional
  public void updateSupplier(
      Long supplierId, @Valid CreateOrUpdateCustomerRequest updateSupplierRequest) {
    securityUtils.checkUserRole(Roles.ADMINISTRADORES);

    final PersonasDto personaDto = personasService.find(supplierId);
    if (Objects.isNull(personaDto)) {
      throw new CustomerNotFoundException();
    }

    proveedoresService.guardarProveedor(
        personaDtoTransformer.transformFromExisting(updateSupplierRequest, personaDto));
  }
}
