package ar.com.gtsoftware.api.impl;

import ar.com.gtsoftware.api.LegalController;
import ar.com.gtsoftware.api.response.IdentificationType;
import ar.com.gtsoftware.dto.domain.LegalGenerosDto;
import ar.com.gtsoftware.dto.domain.LegalTiposDocumentoDto;
import ar.com.gtsoftware.dto.domain.LegalTiposPersoneriaDto;
import ar.com.gtsoftware.search.GenerosSearchFilter;
import ar.com.gtsoftware.search.LegalTiposDocumentoSearchFilter;
import ar.com.gtsoftware.service.LegalGenerosService;
import ar.com.gtsoftware.service.LegalTiposDocumentoService;
import ar.com.gtsoftware.service.LegalTiposPersoneriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class LegalControllerImpl implements LegalController {

    private final LegalTiposPersoneriaService tiposPersoneriaService;
    private final LegalGenerosService generosService;
    private final LegalTiposDocumentoService tiposDocumentoService;

    @Override
    public List<LegalTiposPersoneriaDto> getLegalStatusTypes() {
        return tiposPersoneriaService.findAll();
    }

    @Override
    public List<LegalGenerosDto> getGenders(Long legalStatusId) {
        final GenerosSearchFilter searchFilter = GenerosSearchFilter.builder()
                .idTipoPersoneria(legalStatusId)
                .build();

        return generosService.findAllBySearchFilter(searchFilter);
    }

    @Override
    public List<IdentificationType> getIdentificationTypes(Long legalStatusId) {
        final LegalTiposDocumentoSearchFilter searchFilter = LegalTiposDocumentoSearchFilter.builder()
                .idTipoPersoneria(legalStatusId)
                .build();

        return transformIdentificationTypes(tiposDocumentoService.findAllBySearchFilter(searchFilter));
    }

    private List<IdentificationType> transformIdentificationTypes(List<LegalTiposDocumentoDto> tiposDocumentoDtos) {
        List<IdentificationType> identificationTypes = new ArrayList<>(tiposDocumentoDtos.size());

        for (LegalTiposDocumentoDto tipoDoc : tiposDocumentoDtos) {
            identificationTypes.add(
                    IdentificationType.builder().id(tipoDoc.getId())
                            .identificationTypeName(tipoDoc.getNombreTipoDocumento())
                            .build()
            );
        }

        return identificationTypes;
    }
}
