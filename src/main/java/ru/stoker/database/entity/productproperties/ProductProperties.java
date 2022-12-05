package ru.stoker.database.entity.productproperties;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ru.stoker.database.entity.productproperties.apartment.ApartmentProperties;
import ru.stoker.database.entity.productproperties.car.CarProperties;
import ru.stoker.database.entity.productproperties.notebook.NotebookProperties;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CarProperties.class, name = "car"),
        @JsonSubTypes.Type(value = ApartmentProperties.class, name = "apartment"),
        @JsonSubTypes.Type(value = NotebookProperties.class, name = "notebook")
})
public abstract class ProductProperties {
}
