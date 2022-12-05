package ru.stoker.dto.userestimation;

import lombok.Data;
import ru.stoker.dto.estimation.EstimationDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class UserEstimationDto {

    @NotNull(message = "{user.estimation.userId.not.null}")
    private Long userId;

    @Valid
    @NotNull(message = "{estimation.not.null}")
    private EstimationDto estimation;

}
