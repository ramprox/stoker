package ru.stoker.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stoker.database.entity.Favorite;
import ru.stoker.database.entity.embeddable.FavoriteId;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteId> {

    List<Favorite> findAllByFavoriteIdUserId(Long userId);

}
