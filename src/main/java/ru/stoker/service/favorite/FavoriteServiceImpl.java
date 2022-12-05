package ru.stoker.service.favorite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stoker.database.entity.Advertisement;
import ru.stoker.database.entity.Favorite;
import ru.stoker.database.entity.embeddable.FavoriteId;
import ru.stoker.database.repository.AdvertisementRepository;
import ru.stoker.database.repository.FavoriteRepository;
import ru.stoker.database.repository.UserRepository;
import ru.stoker.dto.advt.AdvtInfo;
import ru.stoker.exceptions.Advt;
import ru.stoker.exceptions.FavoriteEx;
import ru.stoker.exceptions.UserEx;
import ru.stoker.mapper.AdvtMapper;

import java.util.List;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;

    private final UserRepository userRepository;

    private final AdvertisementRepository advertisementRepository;

    private final AdvtMapper advtMapper;

    @Autowired
    public FavoriteServiceImpl(FavoriteRepository favoriteRepository,
                               UserRepository userRepository,
                               AdvertisementRepository advertisementRepository,
                               AdvtMapper advtMapper) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.advertisementRepository = advertisementRepository;
        this.advtMapper = advtMapper;
    }

    @Override
    public void save(Long userId, Long advtId) {
        checkExistUser(userId);
        checkExistAdvt(advtId);
        FavoriteId id = new FavoriteId(userId, advtId);
        Favorite favorite = new Favorite(id);
        favoriteRepository.save(favorite);
    }

    @Override
    public List<AdvtInfo> getFavorites(Long userId) {
        List<Advertisement> advertisements =
                favoriteRepository.findAllByIdUserId(userId)
                        .stream()
                        .map(Favorite::getId)
                        .map(FavoriteId::getAdvertisement)
                        .toList();
        return advtMapper.toListAdvtInfo(advertisements);
    }

    @Override
    @Transactional
    public void deleteById(Long userId, Long advtId) {
        Favorite favorite = favoriteRepository.findById(new FavoriteId(userId, advtId))
                .orElseThrow(() -> new FavoriteEx.NotFoundException(userId, advtId));
        favoriteRepository.delete(favorite);
    }

    private void checkExistUser(Long id) {
        if(!userRepository.existsById(id)) {
            throw new UserEx.NotFoundException(id);
        }
    }

    private void checkExistAdvt(Long id) {
        if(!advertisementRepository.existsById(id)) {
            throw new Advt.NotFoundException(id);
        }
    }

}
