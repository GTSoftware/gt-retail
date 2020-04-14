package ar.com.gtsoftware.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
@ApiModel("The user")
public class User {
    private Integer id;
    @Size(min = 2, message = "Name should have at least 2 characters")
    @NotNull
    @ApiModelProperty(required = true, notes = "The name should be at least 2 characters", example = "Rob")
    private String name;

    @Past(message = "BirthDate should be in the past")
    @ApiModelProperty(required = true, notes = "The birthDate should be in the past", example = "1998-01-12")
    private LocalDate birthDate;
}
