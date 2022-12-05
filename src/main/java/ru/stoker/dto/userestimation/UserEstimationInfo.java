package ru.stoker.dto.userestimation;

import lombok.Data;
import ru.stoker.dto.estimation.EstimationInfo;

@Data
public class UserEstimationInfo {

    private Long ownerUserId;

    private Long userId;

    private EstimationInfo estimation;

}
