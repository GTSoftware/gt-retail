/*
 * Copyright 2018 GT Software.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package ar.com.gtsoftware.service.impl;

import ar.com.gtsoftware.auth.HashPasswordEncoder;
import ar.com.gtsoftware.dao.UsuariosFacade;
import ar.com.gtsoftware.dao.UsuariosGruposFacade;
import ar.com.gtsoftware.domain.Usuarios;
import ar.com.gtsoftware.domain.UsuariosGrupos;
import ar.com.gtsoftware.dto.domain.UsuariosDto;
import ar.com.gtsoftware.dto.domain.UsuariosGruposDto;
import ar.com.gtsoftware.mappers.UsuariosGruposMapper;
import ar.com.gtsoftware.mappers.UsuariosMapper;
import ar.com.gtsoftware.mappers.helper.CycleAvoidingMappingContext;
import ar.com.gtsoftware.search.UsuariosSearchFilter;
import ar.com.gtsoftware.service.BaseEntityService;
import ar.com.gtsoftware.service.UsuariosService;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuariosServiceImpl
    extends BaseEntityService<UsuariosDto, UsuariosSearchFilter, Usuarios>
    implements UsuariosService {

  private final UsuariosFacade facade;
  private final UsuariosGruposFacade gruposFacade;
  private final UsuariosMapper mapper;
  private final UsuariosGruposMapper gruposMapper;

  @Value("${gtretail.default.user.password:Cambiame}")
  private String defaultPassword;

  @Override
  protected UsuariosFacade getFacade() {
    return facade;
  }

  @Override
  protected UsuariosMapper getMapper() {
    return mapper;
  }

  @Override
  @Transactional
  public void changePassword(Long idUsuario, String newPassword) {
    if (newPassword.length() < 6) {
      throw new RuntimeException("La clave no puede contener menos de 6 dìgitos");
    }
    Usuarios usuario = facade.find(idUsuario);
    if (usuario == null) {
      throw new RuntimeException("Usuario inexistente");
    }
    String newPassHashed = new HashPasswordEncoder().encode(newPassword);
    if (usuario.getPassword().equalsIgnoreCase(newPassHashed)) {
      throw new RuntimeException("La clave anterior y la nueva coinciden");
    }
    usuario.setPassword(newPassHashed);
    facade.edit(usuario);
  }

  @Override
  public List<UsuariosGruposDto> obtenerRolesDisponibles() {
    return gruposMapper.entitiesToDtos(gruposFacade.findAll(), new CycleAvoidingMappingContext());
  }

  @Override
  public List<UsuariosGruposDto> obtenerRolesUsuario(@NotNull Long idUsuario) {
    Usuarios usuario = facade.find(idUsuario, "rolesUsuarios");
    if (usuario == null) {
      throw new IllegalArgumentException("No se encontró un usuario con ese Id");
    }
    return gruposMapper.entitiesToDtos(
        usuario.getUsuariosGruposList(), new CycleAvoidingMappingContext());
  }

  @Override
  public void agregarRol(@NotNull Long idUsuario, @NotNull Long idGrupo) {
    Usuarios usuario = facade.find(idUsuario);
    UsuariosGrupos grupo = gruposFacade.find(idGrupo);
    if (usuario.getUsuariosGruposList() == null) {
      usuario.setUsuariosGruposList(new ArrayList<>(1));
    }
    usuario.getUsuariosGruposList().add(grupo);
    facade.edit(usuario);
  }

  @Override
  public void quitarRol(@NotNull Long idUsuario, @NotNull Long idGrupo) {
    Usuarios usuario = facade.find(idUsuario);
    UsuariosGrupos grupo = gruposFacade.find(idGrupo);
    if (usuario.getUsuariosGruposList() == null) {
      return;
    }
    usuario.getUsuariosGruposList().remove(grupo);
    facade.edit(usuario);
  }

  @Override
  public UsuariosDto createOrEdit(@NotNull UsuariosDto dto) {
    Usuarios usuario = mapper.dtoToEntity(dto, new CycleAvoidingMappingContext());
    if (usuario.isNew()) {
      usuario.setPassword(new HashPasswordEncoder().encode(defaultPassword));
    } else {
      Usuarios oldUsuario = facade.find(dto.getId());
      usuario.setPassword(oldUsuario.getPassword());
    }
    return mapper.entityToDto(facade.createOrEdit(usuario), new CycleAvoidingMappingContext());
  }

  @Override
  @Transactional
  public String resetPassword(@NotNull Long idUsuario) {
    Usuarios usuario = facade.find(idUsuario);

    if (usuario != null) {
      usuario.setPassword(new HashPasswordEncoder().encode(defaultPassword));
      facade.createOrEdit(usuario);
      return defaultPassword;
    }

    return null;
  }
}
