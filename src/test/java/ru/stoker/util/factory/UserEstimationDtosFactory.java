package ru.stoker.util.factory;

import ru.stoker.dto.estimation.EstimationDto;
import ru.stoker.dto.estimation.EstimationInfo;
import ru.stoker.dto.userestimation.UpdateUserEstimation;
import ru.stoker.dto.userestimation.UserEstimationDto;
import ru.stoker.dto.userestimation.UserEstimationInfo;

public class UserEstimationDtosFactory {

    public static UserEstimationDto createUserEstimationDto(Long userId, EstimationDto estimationDto) {
        UserEstimationDto userEstimationDto = new UserEstimationDto();
        userEstimationDto.setUserId(userId);
        userEstimationDto.setEstimation(estimationDto);
        return userEstimationDto;
    }

    public static UserEstimationInfo createReadUserEstimationDto(Long ownerId, Long userId, EstimationInfo estimationDto) {
        UserEstimationInfo userEstimationDto = new UserEstimationInfo();
        userEstimationDto.setUserId(userId);
        userEstimationDto.setEstimation(estimationDto);
        userEstimationDto.setOwnerUserId(ownerId);
        return userEstimationDto;
    }

    public static UpdateUserEstimation createUpdateUserEstimation(Long ownerId, UserEstimationDto dto) {
        UpdateUserEstimation update = new UpdateUserEstimation();
        update.setOwnerId(ownerId);
        update.setUserId(dto.getUserId());
        update.setEstimation(dto.getEstimation());
        return update;
    }

}
