package ru.stoker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.stoker.database.entity.UserEstimation;
import ru.stoker.dto.userestimation.UserEstimationDto;
import ru.stoker.dto.userestimation.UserEstimationInfo;

import java.util.List;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(uses = EstimationMapper.class, componentModel = SPRING, injectionStrategy = CONSTRUCTOR)
public interface UserEstimationMapper {

    @Mapping(target = "id.estOwner.id", source = "ownerUserId")
    @Mapping(target = "id.user.id", source = "userEstimationDto.userId")
    UserEstimation toUserEstimation(UserEstimationDto userEstimationDto, Long ownerUserId);

    @Mapping(target = "ownerUserId", source = "id.estOwner.id")
    @Mapping(target = "userId", source = "id.user.id")
    UserEstimationInfo fromUserEstimation(UserEstimation userEstimation);

    List<UserEstimationInfo> fromListUserEstimations(List<UserEstimation> userEstimations);

    @Mapping(target = "id.user.id", source = "userEstimationDto.userId")
    void updateFromUserEstimationDto(UserEstimationDto userEstimationDto,
                                     @MappingTarget UserEstimation userEstimation);

}
