package ru.stoker.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stoker.database.entity.ProductEstimation;
import ru.stoker.database.entity.embeddable.ProductEstimationId;

import java.util.List;

public interface ProductEstimationRepository
        extends JpaRepository<ProductEstimation, ProductEstimationId> {

    List<ProductEstimation> findByIdProductId(Long productId);

}
