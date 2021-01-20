package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.request.FiscalBookRequest;
import io.swagger.annotations.ApiModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@ApiModel(value = "API para obtener los libros de IVA Compras y Ventas")
public interface FiscalBookController {

    @PostMapping(path = "/fiscal/informativeRegime")
    void getInformativeRegime(@Valid @RequestBody FiscalBookRequest filter);
}
