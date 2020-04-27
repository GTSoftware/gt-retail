package ar.com.gtsoftware.search;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LegalTiposDocumentoSearchFilter extends AbstractSearchFilter {

    private Long idTipoPersoneria;

    @Override
    public boolean hasFilter() {
        return idTipoPersoneria != null;
    }
}
