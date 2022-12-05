package ru.stoker.dto.product.productproperties.car;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.stoker.database.entity.productproperties.car.CarState;
import ru.stoker.dto.product.productproperties.ProductPropertiesDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class CarPropertiesDto extends ProductPropertiesDto {

    @Min(value = 1950, message = "{car.year.min}")
    private int manufactureYear;

    @Positive(message = "{car.mileage.positive}")
    private int mileage;

    @Positive(message = "{car.ownerCount.positive}")
    private int ownerCount;

    @NotNull(message = "{car.state.not.null}")
    private CarState carState;

    @Positive(message = "{car.engineVolume.positive}")
    private double engineVolume;

}
