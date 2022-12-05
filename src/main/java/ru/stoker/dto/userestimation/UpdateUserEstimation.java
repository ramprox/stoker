package ru.stoker.dto.userestimation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.stoker.dto.userestimation.UserEstimationDto;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateUserEstimation extends UserEstimationDto {

    @NotNull(message = "{user.estimation.ownerId.not.null}")
    private Long ownerId;

}
