package ar.com.gtsoftware.api.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
