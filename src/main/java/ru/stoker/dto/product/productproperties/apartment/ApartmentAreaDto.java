package ru.stoker.dto.product.productproperties.apartment;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.stoker.dto.util.validators.TotalAreaValid;

import javax.validation.GroupSequence;
import javax.validation.constraints.Positive;
import javax.validation.groups.Default;

@Data
@NoArgsConstructor
@TotalAreaValid(message = "{apartment.totalArea.valid}",
        groups = ApartmentAreaDto.TotalAreaGroup.class)
public class ApartmentAreaDto {

    @Positive(message = "{apartment.totalArea.positive}")
    private double totalArea;

    @Positive(message = "{apartment.kitchenArea.positive}")
    private double kitchenArea;

    @Positive(message = "{apartment.livingArea.positive}")
    private double livingArea;

    @GroupSequence({Default.class, ApartmentAreaDto.TotalAreaGroup.class})
    interface TotalAreaSequence { }

    interface TotalAreaGroup {}

}
