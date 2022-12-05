package ru.stoker.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.stoker.database.entity.Product;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select ad.user.id from Product p " +
            "inner join p.advertisement as ad " +
            "where p.id = :productId")
    Optional<Long> findProductOwnerIdByProductId(Long productId);

}
