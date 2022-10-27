package ru.stoker.database.entity.productproperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.groups.ConvertGroup;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class ApartmentProperties extends ProductProperties {

    @Positive(message = "Количество комнат должно быть больше нуля")
    private int roomCount;

    @NotNull(message = "Площадь квартиры должна быть определена")
    @Valid
    @ConvertGroup(to = ApartmentArea.TotalAreaSequence.class)
    private ApartmentArea apartmentArea;

    @Min(value = 1, message = "Этаж квартиры должен быть больше или равен 1")
    private int floor;

    @Positive(message = "Высота потолков должна быть больше нуля")
    private double ceilingHeight;

    @NotNull(message = "Тип санузла должен быть определен")
    private BathroomType bathroomType;

}
