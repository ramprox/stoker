package ru.stoker.util.factory;

import ru.stoker.dto.estimation.EstimationDto;
import ru.stoker.dto.estimation.EstimationInfo;

import java.time.LocalDateTime;

public class EstimationDtosFactory {

    public static EstimationDto estimationDto(Integer estimation, String comment) {
        EstimationDto estimationDto = new EstimationDto();
        estimationDto.setValue(estimation);
        estimationDto.setComment(comment);
        return estimationDto;
    }

    public static EstimationInfo estimationInfo(EstimationDto estimationDto, LocalDateTime estimatedAt) {
        EstimationInfo estimationInfo = new EstimationInfo();
        estimationInfo.setValue(estimationDto.getValue());
        estimationInfo.setEstimatedAt(estimatedAt);
        estimationInfo.setComment(estimationDto.getComment());
        return estimationInfo;
    }

}
