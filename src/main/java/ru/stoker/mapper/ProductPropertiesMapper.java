package ru.stoker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.SubclassMapping;
import org.mapstruct.SubclassMappings;
import ru.stoker.database.entity.productproperties.ProductProperties;
import ru.stoker.database.entity.productproperties.apartment.ApartmentProperties;
import ru.stoker.database.entity.productproperties.car.CarProperties;
import ru.stoker.database.entity.productproperties.notebook.NotebookProperties;
import ru.stoker.dto.product.productproperties.ProductPropertiesDto;
import ru.stoker.dto.product.productproperties.apartment.ApartmentPropertiesDto;
import ru.stoker.dto.product.productproperties.car.CarPropertiesDto;
import ru.stoker.dto.product.productproperties.notebook.NotebookPropertiesDto;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.SubclassExhaustiveStrategy.RUNTIME_EXCEPTION;

@Mapper(componentModel = SPRING, subclassExhaustiveStrategy = RUNTIME_EXCEPTION)
public interface ProductPropertiesMapper {

    @SubclassMappings({
            @SubclassMapping(target = CarProperties.class, source = CarPropertiesDto.class),
            @SubclassMapping(target = ApartmentProperties.class, source = ApartmentPropertiesDto.class),
            @SubclassMapping(target = NotebookProperties.class, source = NotebookPropertiesDto.class)
    })
    ProductProperties fromProductPropertiesDto(ProductPropertiesDto productPropertiesDto);

    @SubclassMappings({
            @SubclassMapping(target = CarPropertiesDto.class, source = CarProperties.class),
            @SubclassMapping(target = ApartmentPropertiesDto.class, source = ApartmentProperties.class),
            @SubclassMapping(target = NotebookPropertiesDto.class, source = NotebookProperties.class)
    })
    ProductPropertiesDto toProductPropertiesDto(ProductProperties properties);

}
