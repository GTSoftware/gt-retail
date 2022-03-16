package ar.com.gtsoftware.api;

import ar.com.gtsoftware.dto.domain.UsuariosDto;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

public interface UsersController {
  @GetMapping(path = "/users")
  List<UsuariosDto> retrieveAllUsers();

  @GetMapping(path = "/users/{userId}")
  UsuariosDto retrieveUser(@PathVariable Long userId);

  @PutMapping(path = "/users/{userId}/reset-password")
  ChangePasswordRequest resetPassword(@PathVariable Long userId);

  @PutMapping(path = "/users/{userId}/change-password")
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  void changePassword(
      @PathVariable Long userId, @RequestBody @Valid ChangePasswordRequest newPassword);
}
