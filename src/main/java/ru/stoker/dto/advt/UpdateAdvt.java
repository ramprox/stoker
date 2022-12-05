package ru.stoker.dto.advt;

import lombok.Data;
import ru.stoker.dto.product.UpdateProduct;
import ru.stoker.dto.util.validgroups.OnUpdate;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateAdvt {

    @NotNull(message = "{advt.id.not.null}")
    private Long id;

    @NotBlank(message = "{advt.name.not.blank}")
    private String name;

    @Valid
    @NotNull(message = "{advt.product.not.null}")
    private UpdateProduct product;
}
