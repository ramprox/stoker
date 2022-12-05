package ru.stoker.dto.userestimation;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserEstimationIdDto {

    @NotNull(message = "{user.estimation.ownerId.not.null}")
    private Long ownerUserId;

    @NotNull(message = "{user.estimation.userId.not.null}")
    private Long userId;

}
