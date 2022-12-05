package ru.stoker.dto.product.productproperties.apartment;

import lombok.Data;
import ru.stoker.database.entity.productproperties.apartment.ApartmentArea;
import ru.stoker.database.entity.productproperties.apartment.BathroomType;
import ru.stoker.dto.product.productproperties.ProductPropertiesDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.groups.ConvertGroup;

@Data
public class ApartmentPropertiesDto extends ProductPropertiesDto {

    @Positive(message = "{apartment.roomCount.positive}")
    private int roomCount;

    @NotNull(message = "{apartment.area.not.null}")
    @Valid
    @ConvertGroup(to = ApartmentAreaDto.TotalAreaSequence.class)
    private ApartmentArea apartmentArea;

    @Min(value = 1, message = "{apartment.floor.min}")
    private int floor;

    @Positive(message = "{apartment.ceiling.height.positive}")
    private double ceilingHeight;

    @NotNull(message = "{apartment.bathroomType.not.null}")
    private BathroomType bathroomType;

}
