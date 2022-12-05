package ru.stoker.dto.product.productproperties;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ru.stoker.dto.product.productproperties.apartment.ApartmentPropertiesDto;
import ru.stoker.dto.product.productproperties.car.CarPropertiesDto;
import ru.stoker.dto.product.productproperties.notebook.NotebookPropertiesDto;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CarPropertiesDto.class, name = "car"),
        @JsonSubTypes.Type(value = ApartmentPropertiesDto.class, name = "apartment"),
        @JsonSubTypes.Type(value = NotebookPropertiesDto.class, name = "notebook")
})
public abstract class ProductPropertiesDto {
}
