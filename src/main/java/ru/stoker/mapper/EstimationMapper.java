package ru.stoker.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import ru.stoker.database.entity.embeddable.Estimation;
import ru.stoker.dto.estimation.EstimationDto;
import ru.stoker.dto.estimation.EstimationInfo;

import java.time.LocalDateTime;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface EstimationMapper {

    Estimation fromEstimationDto(EstimationDto estimationDto);

    @AfterMapping
    default void afterMapping(@MappingTarget Estimation estimation) {
        if(estimation.getEstimatedAt() == null) {
            estimation.setEstimatedAt(LocalDateTime.now());
        }
    }

    EstimationInfo toEstimationInfo(Estimation estimation);

    void updateFromEstimationDto(EstimationDto estimationDto, @MappingTarget Estimation estimation);

}
