package ru.stoker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.stoker.database.entity.ProductEstimation;
import ru.stoker.dto.productestimaton.ProductEstimationDto;
import ru.stoker.dto.productestimaton.ProductEstimationInfo;

import java.util.List;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(uses = EstimationMapper.class, componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
public interface ProductEstimationMapper {

    @Mapping(target = "id.estOwner.id", source = "userId")
    @Mapping(target = "id.product.id", source = "productEstimationDto.productId")
    ProductEstimation toProductEstimation(ProductEstimationDto productEstimationDto, Long userId);

    @Mapping(target = "userId", source = "id.estOwner.id")
    @Mapping(target = "productId", source = "id.product.id")
    ProductEstimationInfo fromProductEstimation(ProductEstimation productEstimation);

    List<ProductEstimationInfo> fromListProductEstimation(List<ProductEstimation> productEstimations);

    void updateFromProductEstimationDto(ProductEstimationDto estimationDto,
                                        @MappingTarget ProductEstimation productEstimation);

}
