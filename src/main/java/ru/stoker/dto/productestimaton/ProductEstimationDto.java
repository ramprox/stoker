package ru.stoker.dto.productestimaton;

import lombok.Data;
import ru.stoker.dto.estimation.EstimationDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class ProductEstimationDto {

    @NotNull(message = "{product.id.not.null}")
    private Long productId;

    @Valid
    @NotNull(message = "{estimation.not.null}")
    private EstimationDto estimation;

}
