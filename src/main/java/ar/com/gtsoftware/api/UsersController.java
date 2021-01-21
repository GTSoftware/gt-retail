package ar.com.gtsoftware.api;

import ar.com.gtsoftware.dto.domain.UsuariosDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiResponse;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ApiModel(description = "User management")
public interface UsersController {
  @GetMapping(path = "/users")
  List<UsuariosDto> retrieveAllUsers();

  @GetMapping(path = "/users/{userId}")
  UsuariosDto retrieveUser(@PathVariable Long userId);

  @PutMapping(path = "/users/{userId}/reset-password")
  ChangePasswordRequest resetPassword(@PathVariable Long userId);

  @PutMapping(path = "/users/{userId}/change-password")
  @ResponseStatus(value = HttpStatus.NO_CONTENT)
  @ApiResponse(code = 204, message = "No Content")
  void changePassword(
      @PathVariable Long userId, @RequestBody @Valid ChangePasswordRequest newPassword);
}
