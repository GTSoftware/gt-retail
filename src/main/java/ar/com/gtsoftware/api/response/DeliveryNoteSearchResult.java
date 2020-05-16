package ar.com.gtsoftware.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class DeliveryNoteSearchResult {
    private Long deliveryNoteId;
    private LocalDateTime createdDate;
    private String origin;
    private String destination;
    private String observations;
    private String type;
    private String user;

}
