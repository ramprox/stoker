package ru.stoker.dto.productestimaton;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.stoker.dto.productestimaton.ProductEstimationDto;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateProductEstimation extends ProductEstimationDto {

    @NotNull(message = "{user.id.not.null}")
    private Long userId;

}
