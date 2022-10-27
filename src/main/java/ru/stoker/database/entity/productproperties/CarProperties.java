package ru.stoker.database.entity.productproperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class CarProperties extends ProductProperties {

    @Positive(message = "Год выпуска должен быть больше нуля")
    private int manufactureYear;

    @Positive(message = "Пробег должен быть больше нуля")
    private int mileage;

    @Positive(message = "Количество владельцев должно быть больше нуля")
    private int ownerCount;

    @NotNull(message = "Состояние машины должно быть определено")
    private CarState carState;

    @Positive(message = "Объем двигателя должен быть больше нуля")
    private double engineVolume;

}
