package ar.com.gtsoftware.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@AllArgsConstructor
public class AddDeliveryNoteRequest {
    @NotNull
    private final Long deliveryTypeId;

    private final Long internalOriginId;
    private final Long externalOriginId;
    private final Long internalDestinationId;
    private final Long externalDestinationId;

    private final String observations;
    @Valid
    @NotNull
    private final List<DeliveryNoteItem> deliveryNoteItems;

}
