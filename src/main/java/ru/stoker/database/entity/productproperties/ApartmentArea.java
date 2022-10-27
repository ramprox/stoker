package ru.stoker.database.entity.productproperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.stoker.database.util.TotalAreaValid;

import javax.validation.GroupSequence;
import javax.validation.constraints.Positive;
import javax.validation.groups.Default;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@TotalAreaValid(message = "Общая площадь не может быть меньше суммы всех остальных",
        groups = ApartmentArea.TotalAreaGroup.class)
public class ApartmentArea {

    @Positive(message = "Общая площадь должна быть больше нуля")
    private double totalArea;

    @Positive(message = "Площадь кухни должна быть больше нуля")
    private double kitchenArea;

    @Positive(message = "Жилая площадь должна быть больше нуля")
    private double livingArea;

    @GroupSequence({Default.class, TotalAreaGroup.class})
    interface TotalAreaSequence { }

    interface TotalAreaGroup {}

}
