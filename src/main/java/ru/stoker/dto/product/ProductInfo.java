package ru.stoker.dto.product;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.stoker.dto.product.productproperties.ProductPropertiesDto;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(exclude = "price")
public class ProductInfo {

    private Long categoryId;

    private BigDecimal price;

    private String description;

    private ProductPropertiesDto properties;

    private List<Long> attachments;

}
