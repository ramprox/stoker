package ru.stoker.dto.product;

import lombok.Data;
import ru.stoker.dto.product.productproperties.ProductPropertiesDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class CreateProduct {

    @NotNull(message = "{category.id.not.null}")
    private Long categoryId;

    @NotNull(message = "{price.not.null}")
    @Min(value = 0, message = "{price.min}")
    private BigDecimal price;

    private String description;

    @Valid
    private ProductPropertiesDto properties;

}
