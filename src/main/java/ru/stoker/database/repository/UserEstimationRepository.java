package ru.stoker.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stoker.database.entity.UserEstimation;
import ru.stoker.database.entity.embeddable.UserEstimationId;

import java.util.List;

public interface UserEstimationRepository
        extends JpaRepository<UserEstimation, UserEstimationId> {

    List<UserEstimation> findAllByUserEstimationIdEstimatedUserId(Long id);

}
