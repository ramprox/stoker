package ru.stoker.service.advertisement;

import ru.stoker.dto.advt.AdvtInfo;
import ru.stoker.dto.advt.CreateAdvt;
import ru.stoker.dto.advt.UpdateAdvt;

import java.util.List;

public interface AdvertisementService {

    AdvtInfo getById(Long id);

    List<AdvtInfo> findAll(Long categoryId);

    void deleteById(Long id);

    void deleteAllByUserId(Long userId);

    AdvtInfo save(Long ownerId, CreateAdvt advtDto);

    AdvtInfo update(Long ownerId, UpdateAdvt advtDto);

}
