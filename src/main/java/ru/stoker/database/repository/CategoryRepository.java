package ru.stoker.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stoker.database.entity.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByParentId(Long id);

    List<Category> findByParentIsNull();

}
