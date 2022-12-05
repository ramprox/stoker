package ru.stoker.dto.advt;

import lombok.Data;
import ru.stoker.dto.product.CreateProduct;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateAdvt {

    @NotBlank(message = "{advt.name.not.blank}")
    private String name;

    @Valid
    @NotNull(message = "{advt.product.not.null}")
    private CreateProduct product;

}
