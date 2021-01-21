/*
 * Copyright 2015 GT Software.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ar.com.gtsoftware.service.impl;

import ar.com.gtsoftware.api.exception.CustomerValidationException;
import ar.com.gtsoftware.dao.*;
import ar.com.gtsoftware.domain.Personas;
import ar.com.gtsoftware.domain.PersonasTelefonos;
import ar.com.gtsoftware.dto.domain.PersonasDto;
import ar.com.gtsoftware.mappers.PersonasMapper;
import ar.com.gtsoftware.mappers.helper.CycleAvoidingMappingContext;
import ar.com.gtsoftware.search.PersonasSearchFilter;
import ar.com.gtsoftware.service.ClientesService;
import ar.com.gtsoftware.validators.ValidadorCUIT;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientesServiceImpl implements ClientesService {

  private final PersonasFacade personasFacade;
  private final PersonasTelefonosFacade telefonosFacade;
  private final PersonasMapper personasMapper;
  private final LegalGenerosFacade legalGenerosFacade;
  private final UbicacionLocalidadesFacade localidadesFacade;
  private final FiscalResponsabilidadesIvaFacade fiscalResponsabilidadesIvaFacade;
  private final SucursalesFacade sucursalesFacade;
  private final LegalTiposDocumentoFacade legalTiposDocumentoFacade;

  @Override
  @Transactional
  public PersonasDto guardarCliente(PersonasDto clienteDto) {

    Personas cliente = personasMapper.dtoToEntity(clienteDto, new CycleAvoidingMappingContext());

    completeCustomer(cliente);
    validarCliente(cliente);
    formatDatosCliente(cliente);
    if (cliente.isNew()) {

      cliente.setFechaAlta(LocalDateTime.now());
      cliente.setActivo(true);
      cliente.setCliente(true);
    }
    personasFacade.createOrEdit(cliente);

    return personasMapper.entityToDto(
        personasFacade.find(cliente.getId()), new CycleAvoidingMappingContext());
  }

  private void completeCustomer(Personas cliente) {
    cliente.setIdGenero(legalGenerosFacade.find(cliente.getIdGenero().getId()));
    cliente.setIdLocalidad(localidadesFacade.find(cliente.getIdLocalidad().getId()));
    cliente.setIdProvincia(cliente.getIdLocalidad().getIdProvincia());
    cliente.setIdPais(cliente.getIdLocalidad().getIdProvincia().getIdPais());
    cliente.setIdResponsabilidadIva(
        fiscalResponsabilidadesIvaFacade.find(cliente.getIdResponsabilidadIva().getId()));
    cliente.setIdSucursal(sucursalesFacade.find(cliente.getIdSucursal().getId()));
    cliente.setIdTipoDocumento(
        legalTiposDocumentoFacade.find(cliente.getIdTipoDocumento().getId()));
    cliente.setIdTipoPersoneria(cliente.getIdTipoDocumento().getIdTipoPersoneria());
    if (cliente.getPersonasTelefonosList() != null) {
      for (PersonasTelefonos tel : cliente.getPersonasTelefonosList()) {
        tel.setIdPersona(cliente);
      }
    }
  }

  private void formatDatosCliente(Personas cliente) {
    cliente.setRazonSocial(
        formatRazonSocial(
            cliente.getRazonSocial(),
            cliente.getApellidos(),
            cliente.getNombres(),
            cliente.getIdTipoPersoneria().getId()));
  }

  private String formatRazonSocial(
      String razonSocialCliente, String apellidos, String nombres, long tipoPersoneria) {
    String razonSocial = razonSocialCliente;
    if (tipoPersoneria == 1) { // Persona física
      razonSocial = String.format("%s, %s", apellidos, nombres);
    }
    return razonSocial;
  }

  private void validarCliente(Personas cliente) {
    try {
      long documento = Long.parseLong(cliente.getDocumento());
      if (documento <= 0) {
        throw new CustomerValidationException(
            "El documento debe ser un número positivo distinto de 0!");
      }
    } catch (NumberFormatException nfe) {
      throw new CustomerValidationException("El documento debe ser un número!");
    }
    // Es responsable inscripto y tiene cargado DNI
    if (cliente.getIdResponsabilidadIva().getId() == 2
        && cliente.getIdTipoDocumento().getFiscalCodigoTipoDocumento() != 80) {
      throw new CustomerValidationException(
          "Se debe cargar tipo de documento como CUIT para Responsables Inscriptos");
    }
    if (cliente.getIdTipoDocumento().getFiscalCodigoTipoDocumento() == 80
        && !ValidadorCUIT.getValidate(cliente.getDocumento())) {
      throw new CustomerValidationException("El número de CUIT ingresado no es válido!");
    }
    PersonasSearchFilter psf =
        PersonasSearchFilter.builder()
            .idTipoDocumento(cliente.getIdTipoDocumento().getId())
            .documento(cliente.getDocumento())
            .activo(true)
            .cliente(true)
            .build();
    int result = personasFacade.countBySearchFilter(psf);
    if (cliente.isNew()) {
      if (result > 0) {
        throw new CustomerValidationException("Ya existe un cliente con ese número de documento!");
      }
    } else if (personasFacade.existePersonaRepetida(cliente)) {
      throw new CustomerValidationException("Ya existe un cliente con ese número de documento!");
    }
  }

  @Override
  public PersonasDto find(Long id) {
    return personasMapper.entityToDto(personasFacade.find(id), new CycleAvoidingMappingContext());
  }

  public void guardarTelefono(PersonasTelefonos telefono) {
    telefonosFacade.createOrEdit(telefono);
  }

  public void borrarTelefono(PersonasTelefonos telefono) {
    telefonosFacade.remove(telefono);
  }
}
