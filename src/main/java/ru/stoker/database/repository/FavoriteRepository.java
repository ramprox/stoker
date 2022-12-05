package ru.stoker.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.stoker.database.entity.Favorite;
import ru.stoker.database.entity.embeddable.FavoriteId;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteId> {

    @Query("select distinct f from Favorite f " +
            "join fetch f.id.advertisement ad " +
            "join fetch ad.product p " +
            "left join fetch p.attachments at " +
            "where f.id.user.id = :userId")
    List<Favorite> findAllByIdUserId(Long userId);

}
