package ar.com.gtsoftware.api.response;

import java.util.List;
import lombok.Builder;

@Builder
public record PaginatedResponse<T>(List<T> data, int totalResults) {}
