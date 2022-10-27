package ru.stoker.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stoker.database.entity.Advertisement;

import java.util.Optional;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {

    <T> Optional<T> findById(Long id, Class<T> type);

}
