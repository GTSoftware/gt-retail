package ar.com.gtsoftware.api.impl;

import static ar.com.gtsoftware.auth.Roles.ADMINISTRADORES;

import ar.com.gtsoftware.api.ChangePasswordRequest;
import ar.com.gtsoftware.api.UsersController;
import ar.com.gtsoftware.dto.domain.UsuariosDto;
import ar.com.gtsoftware.service.UsuariosService;
import ar.com.gtsoftware.utils.SecurityUtils;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class UsersControllerImpl implements UsersController {

  private final UsuariosService usuariosService;
  private final SecurityUtils securityUtils;

  @Override
  public List<UsuariosDto> retrieveAllUsers() {
    return usuariosService.findAll();
  }

  @Override
  public UsuariosDto retrieveUser(Long userId) {
    return usuariosService.find(userId);
  }

  @Override
  @Transactional
  public ChangePasswordRequest resetPassword(Long userId) {
    if (ableToChangePassword(userId)) {
      String defaultPassword = usuariosService.resetPassword(userId);
      return new ChangePasswordRequest(defaultPassword);
    } else {
      throw new RuntimeException("Solo un usuario administrador puede realizar esta tarea");
    }
  }

  @Override
  @Transactional
  public void changePassword(Long userId, @Valid ChangePasswordRequest newPassword) {
    if (ableToChangePassword(userId)) {
      usuariosService.changePassword(userId, newPassword.getNewPassword());
    } else {
      throw new RuntimeException("Solo un usuario administrador puede realizar esta tarea");
    }
  }

  private boolean ableToChangePassword(Long userId) {
    final boolean isAdminUser = securityUtils.userHasRole(ADMINISTRADORES);
    final boolean isSameUser = Objects.equals(securityUtils.getUserDetails().getId(), userId);

    return isAdminUser || isSameUser;
  }
}
