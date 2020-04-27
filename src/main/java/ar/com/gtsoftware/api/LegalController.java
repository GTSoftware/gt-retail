package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.response.IdentificationType;
import ar.com.gtsoftware.dto.domain.LegalGenerosDto;
import ar.com.gtsoftware.dto.domain.LegalTiposPersoneriaDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface LegalController {

    @GetMapping(path = "/legal/status-types")
    List<LegalTiposPersoneriaDto> getLegalStatusTypes();

    @GetMapping(path = "/legal/genders")
    List<LegalGenerosDto> getGenders(@RequestParam(name = "legalStatusId") Long legalStatusId);

    @GetMapping(path = "/legal/identification-types")
    List<IdentificationType> getIdentificationTypes(@RequestParam(name = "legalStatusId") Long legalStatusId);
}
