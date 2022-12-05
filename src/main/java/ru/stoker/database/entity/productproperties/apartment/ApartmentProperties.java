package ru.stoker.database.entity.productproperties.apartment;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.stoker.database.entity.productproperties.ProductProperties;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class ApartmentProperties extends ProductProperties {

    private int roomCount;

    private ApartmentArea apartmentArea;

    private int floor;

    private double ceilingHeight;

    private BathroomType bathroomType;

}
