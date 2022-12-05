package ru.stoker.service.userestimation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stoker.database.entity.UserEstimation;
import ru.stoker.database.entity.embeddable.UserEstimationId;
import ru.stoker.database.repository.UserEstimationRepository;
import ru.stoker.database.repository.UserRepository;
import ru.stoker.dto.userestimation.UserEstimationDto;
import ru.stoker.dto.userestimation.UserEstimationInfo;
import ru.stoker.exceptions.UserEx;
import ru.stoker.exceptions.UserEstimationEx;
import ru.stoker.exceptions.UserEstimationEx.NotFoundException;
import ru.stoker.mapper.UserEstimationMapper;

import java.util.List;

@Service
public class UserEstimationServiceImpl implements UserEstimationService {

    private final UserEstimationRepository userEstimationRepository;

    private final UserRepository userRepository;

    private final UserEstimationMapper userEstimationMapper;

    @Autowired
    public UserEstimationServiceImpl(UserEstimationRepository userEstimationRepository,
                                     UserRepository userRepository,
                                     UserEstimationMapper userEstimationMapper) {
        this.userEstimationRepository = userEstimationRepository;
        this.userRepository = userRepository;
        this.userEstimationMapper = userEstimationMapper;
    }

    @Override
    @Transactional
    public UserEstimationInfo estimate(Long ownerId, UserEstimationDto userEstimationDto) {
        Long userId = userEstimationDto.getUserId();
        checkUserNotEstimateSelf(ownerId, userId);
        checkUserExist(userId);
        checkNotExistEstimation(ownerId, userId);
        UserEstimation userEstimation = userEstimationMapper.toUserEstimation(userEstimationDto, ownerId);
        userEstimationRepository.save(userEstimation);
        return userEstimationMapper.fromUserEstimation(userEstimation);
    }

    @Override
    public UserEstimationInfo getById(Long ownerId, Long userId) {
        UserEstimation userEstimation = findById(ownerId, userId);
        return userEstimationMapper.fromUserEstimation(userEstimation);
    }

    @Override
    public List<UserEstimationInfo> getAllByUserId(Long userId) {
        List<UserEstimation> userEstimations = userEstimationRepository.findByIdUserId(userId);
        return userEstimationMapper.fromListUserEstimations(userEstimations);
    }

    @Override
    @Transactional
    public UserEstimationInfo update(Long ownerId, UserEstimationDto userEstimationDto) {
        Long userId = userEstimationDto.getUserId();
        checkUserNotEstimateSelf(ownerId, userId);
        checkUserExist(userId);
        UserEstimation userEstimation = findById(ownerId, userId);
        userEstimationMapper.updateFromUserEstimationDto(userEstimationDto, userEstimation);
        return userEstimationMapper.fromUserEstimation(userEstimation);
    }

    @Override
    @Transactional
    public void deleteByUserId(Long ownerId, Long userId) {
        UserEstimation estimation = findById(ownerId, userId);
        userEstimationRepository.delete(estimation);
    }

    private UserEstimation findById(Long ownerId, Long userId) {
        UserEstimationId userEstimationId = new UserEstimationId(ownerId, userId);
        return userEstimationRepository.findById(userEstimationId)
                .orElseThrow(() -> new NotFoundException(ownerId, userId));
    }

    private void checkUserNotEstimateSelf(Long ownerUserId, Long userId) {
        if(ownerUserId.equals(userId)) {
            throw new UserEstimationEx.SelfEstimationException(ownerUserId, userId);
        }
    }

    private void checkUserExist(Long userId) {
        if(!userRepository.existsById(userId)) {
            throw new UserEx.NotFoundException(userId);
        }
    }

    private void checkNotExistEstimation(Long ownerId, Long userId) {
        UserEstimationId userEstimationId = new UserEstimationId(ownerId, userId);
        if(userEstimationRepository.existsById(userEstimationId)) {
            throw new UserEstimationEx.AlreadyExistException(ownerId, userId);
        }
    }

}
