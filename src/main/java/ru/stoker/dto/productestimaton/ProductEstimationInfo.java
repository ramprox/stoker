package ru.stoker.dto.productestimaton;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.stoker.dto.estimation.EstimationInfo;

@Data
@EqualsAndHashCode
public class ProductEstimationInfo {

    private Long userId;

    private Long productId;

    private EstimationInfo estimation;

}
