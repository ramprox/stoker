package ru.stoker.service.favorite;

import ru.stoker.dto.advt.AdvtInfo;

import java.util.List;

public interface FavoriteService {

    void save(Long userId, Long advtId);

    List<AdvtInfo> getFavorites(Long userId);

    void deleteById(Long userId, Long advtId);

}
