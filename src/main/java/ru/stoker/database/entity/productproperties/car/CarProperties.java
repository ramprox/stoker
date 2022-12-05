package ru.stoker.database.entity.productproperties.car;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.stoker.database.entity.productproperties.ProductProperties;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class CarProperties extends ProductProperties {

    private int manufactureYear;

    private int mileage;

    private int ownerCount;

    private CarState carState;

    private double engineVolume;

}
