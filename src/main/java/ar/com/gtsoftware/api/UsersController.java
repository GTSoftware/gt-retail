package ar.com.gtsoftware.api;

import ar.com.gtsoftware.dto.domain.UsuariosDto;
import ar.com.gtsoftware.service.UsuariosService;
import ar.com.gtsoftware.utils.SecurityUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

import static ar.com.gtsoftware.auth.Roles.ADMINISTRADORES;

@RestController
@RequiredArgsConstructor
@ApiModel(description = "User management")
public class UsersController {

    private final UsuariosService usuariosService;
    private final SecurityUtils securityUtils;
    private final Logger logger = LoggerFactory.getLogger(UsersController.class);


    @GetMapping(path = "/users")
    public List<UsuariosDto> retrieveAllUsers() {
        return usuariosService.findAll();
    }

    @GetMapping(path = "/users/{userId}")
    public UsuariosDto retrieveUser(@PathVariable Long userId) {
        return usuariosService.find(userId);
    }

    @PutMapping(path = "/users/{userId}/reset-password")
    public ChangePasswordRequest resetPassword(@PathVariable Long userId) {
        if (ableToChangePassword(userId)) {
            String defaultPassword = usuariosService.resetPassword(userId);
            return new ChangePasswordRequest(defaultPassword);
        } else {
            throw new RuntimeException("Solo un usuario administrador puede realizar esta tarea");
        }
    }

    @PutMapping(path = "/users/{userId}/change-password")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @ApiResponse(code = 204, message = "No Content")
    public void changePassword(@PathVariable Long userId,
                               @RequestBody @Valid ChangePasswordRequest newPassword) {
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
