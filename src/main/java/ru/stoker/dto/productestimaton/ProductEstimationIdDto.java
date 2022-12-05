package ru.stoker.dto.productestimaton;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ProductEstimationIdDto {

    @NotNull(message = "{user.id.not.null}")
    private Long userId;

    @NotNull(message = "{product.id.not.null}")
    private Long productId;

}
