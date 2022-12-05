package ru.stoker.dto.estimation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
public class EstimationDto {

    @NotNull(message = "{estimation.value.not.null}")
    @Range(min = 1, max = 10, message = "{estimation.range}")
    private Integer value;

    private String comment;

}
