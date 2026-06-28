package ar.com.gtsoftware.api.request.cashbox;

import java.util.List;

public record CloseBoxRequest(String notes, List<CloseBoxDetail> closeBoxDetails) {}
