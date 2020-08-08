package ar.com.gtsoftware.api.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class SalesToPayRequest {

    @NotNull
    @NotEmpty
    private List<Long> salesIds;
}
