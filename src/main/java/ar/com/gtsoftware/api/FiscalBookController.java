package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.request.DigitalFiscalBookRequest;
import io.swagger.annotations.ApiModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@ApiModel(value = "API para obtener los libros de IVA Compras y Ventas como así tambien el régimen informativo de compras y ventas")
public interface FiscalBookController {

    @PostMapping(path = "/fiscal/digital-book")
    void getInformativeRegime(@Valid @RequestBody DigitalFiscalBookRequest filter);
}
