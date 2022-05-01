package ar.com.gtsoftware.api.response;

import ar.com.gtsoftware.dto.domain.FiscalResponsabilidadesIvaDto;
import ar.com.gtsoftware.dto.domain.LegalGenerosDto;
import ar.com.gtsoftware.dto.domain.LegalTiposPersoneriaDto;
import ar.com.gtsoftware.dto.domain.UbicacionLocalidadesDto;
import ar.com.gtsoftware.dto.domain.UbicacionPaisesDto;
import ar.com.gtsoftware.dto.domain.UbicacionProvinciasDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerResponse {
  private Long customerId;
  private String email;
  private String businessName;
  private String fantasyName;
  private String address;
  private String identification;
  private Long branchId;
  private String responsabilidadIVA;
  private List<Phone> phones;
  private Integer version;
  private UbicacionPaisesDto pais;
  private UbicacionProvinciasDto provincia;
  private UbicacionLocalidadesDto localidad;
  private LegalGenerosDto genero;
  private LegalTiposPersoneriaDto tipoPersoneria;
  private IdentificationType tipoDocumento;
  private FiscalResponsabilidadesIvaDto tipoResponsableIva;
  private String documento;
  private String calle;
  private String altura;
  private String depto;
  private String piso;
  private String nombres;
  private String apellidos;
  private String razonSocial;
  private boolean activo;
}
