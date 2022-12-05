package ru.stoker.service.userestimation;

import ru.stoker.dto.userestimation.UserEstimationDto;
import ru.stoker.dto.userestimation.UserEstimationInfo;

import java.util.List;

public interface UserEstimationService {

    UserEstimationInfo estimate(Long ownerId, UserEstimationDto userEstimationDto);

    UserEstimationInfo getById(Long ownerId, Long userId);

    List<UserEstimationInfo> getAllByUserId(Long userId);

    UserEstimationInfo update(Long ownerId, UserEstimationDto userEstimationDto);

    void deleteByUserId(Long ownerId, Long userId);

}
