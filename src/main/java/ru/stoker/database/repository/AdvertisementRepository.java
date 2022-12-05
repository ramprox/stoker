package ru.stoker.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.stoker.database.entity.Advertisement;

import java.util.List;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {

    @Query("select distinct ad from Advertisement ad " +
            "join fetch ad.product p " +
            "left join fetch p.attachments att " +
            "where p.category.id = :categoryId")
    List<Advertisement> findByProductCategoryId(Long categoryId);

    @Query("select distinct ad from Advertisement ad " +
            "join fetch ad.product p " +
            "left join fetch p.attachments att " +
            "where ad.user.id = :userId")
    List<Advertisement> findByUserId(Long userId);

}
