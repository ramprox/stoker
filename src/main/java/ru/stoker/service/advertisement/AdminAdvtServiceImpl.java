package ru.stoker.service.advertisement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stoker.database.repository.UserRepository;
import ru.stoker.dto.advt.AdminCreateAdvt;
import ru.stoker.dto.advt.AdminUpdateAdvt;
import ru.stoker.dto.advt.AdvtInfo;
import ru.stoker.exceptions.UserEx;

@Service
@RequiredArgsConstructor
public class AdminAdvtServiceImpl implements AdminAdvtService {

    private final AdvertisementService advertisementService;

    private final UserRepository userRepository;

    @Override
    @Transactional
    public AdvtInfo create(AdminCreateAdvt advtDto) {
        checkUserExist(advtDto.getUserId());
        return advertisementService.save(advtDto.getUserId(), advtDto);
    }

    @Override
    @Transactional
    public AdvtInfo update(AdminUpdateAdvt advtDto) {
        checkUserExist(advtDto.getUserId());
        return advertisementService.update(advtDto.getUserId(), advtDto);
    }

    @Override
    public void deleteById(Long id) {
        advertisementService.deleteById(id);
    }

    private void checkUserExist(Long userId) {
        if(!userRepository.existsById(userId)) {
            throw new UserEx.NotFoundException(userId);
        }
    }

}
