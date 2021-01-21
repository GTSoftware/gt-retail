package ar.com.gtsoftware.api.request;

import ar.com.gtsoftware.dto.domain.PersonasTelefonosDto;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NewCustomerRequest {

  @Pattern(
      regexp =
          "^$|^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\."
              + "[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@"
              + "(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
      message = "Debe ser un mail válido Ej: alguien@host.com.")
  private String email;

  @NotNull
  @Size(min = 1, max = 200)
  private String razonSocial;

  @Size(max = 60)
  private String apellidos;

  @Size(max = 60)
  private String nombres;

  @Size(max = 200)
  private String nombreFantasia;

  @Size(max = 100)
  @NotNull
  private String calle;

  @Size(max = 50)
  @NotNull
  private String altura;

  @Size(max = 3)
  @NotNull
  private String piso;

  @Size(max = 5)
  @NotNull
  private String depto;

  @NotNull
  @Size(min = 1, max = 13)
  private String documento;

  @NotNull private Long provinciaId;
  @NotNull private Long paisId;
  @NotNull private Long localidadId;
  @NotNull private Long tipoPersoneriaId;
  @NotNull private Long tipoDocumentoId;
  @NotNull private Long generoId;
  @NotNull private Long responsabilidadIvaId;
  @Valid private List<PersonasTelefonosDto> telefonos;
}
