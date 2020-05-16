package ar.com.gtsoftware.api;

import ar.com.gtsoftware.api.request.AddDeliveryItemRequest;
import ar.com.gtsoftware.api.request.AddDeliveryNoteRequest;
import ar.com.gtsoftware.api.request.PaginatedSearchRequest;
import ar.com.gtsoftware.api.response.*;
import ar.com.gtsoftware.dto.domain.RemitoTipoMovimientoDto;
import ar.com.gtsoftware.search.RemitoSearchFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

public interface DeliveryNotesController {

    @GetMapping(path = "/delivery-notes/warehouses")
    List<Warehouse> getWarehouses();

    @GetMapping(path = "/delivery-notes/delivery-types")
    List<RemitoTipoMovimientoDto> getDeliveryTypes();

    @PostMapping(path = "/delivery-notes/add-product")
    DeliveryItemResponse addProduct(@RequestBody @Valid AddDeliveryItemRequest addDeliveryItemRequest);

    @PostMapping(path = "/delivery-notes/delivery-note")
    Long saveDeliveryNote(@RequestBody @Valid AddDeliveryNoteRequest addDeliveryNoteRequest);

    @PostMapping(path = "/delivery-notes/search")
    PaginatedResponse<DeliveryNoteSearchResult> findBySearchFilter(@Valid @RequestBody PaginatedSearchRequest<RemitoSearchFilter> searchRequest);

    @GetMapping(path = "/delivery-notes/{deliveryNoteId}/details")
    List<DeliveryNoteDetail> getDeliveryNoteDetails(@PathVariable Long deliveryNoteId);
}
