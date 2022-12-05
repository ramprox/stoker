package ru.stoker.service.advertisement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.stoker.database.repository.UserRepository;
import ru.stoker.dto.advt.AdminCreateAdvt;
import ru.stoker.dto.advt.AdminUpdateAdvt;
import ru.stoker.dto.advt.AdvtInfo;
import ru.stoker.exceptions.UserEx;

import java.util.List;

@Service
public class AdminAdvtServiceImpl implements AdminAdvtService {

    private final AdvertisementService advertisementService;

    private final UserRepository userRepository;

    @Autowired
    public AdminAdvtServiceImpl(AdvertisementService advertisementService,
                                UserRepository userRepository) {
        this.advertisementService = advertisementService;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public AdvtInfo create(AdminCreateAdvt advtDto, List<MultipartFile> files) {
        checkUserExist(advtDto.getUserId());
        return advertisementService.save(advtDto.getUserId(), advtDto, files);
    }

    @Override
    @Transactional
    public AdvtInfo update(AdminUpdateAdvt advtDto, List<MultipartFile> files) {
        checkUserExist(advtDto.getUserId());
        return advertisementService.update(advtDto.getUserId(), advtDto, files);
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
