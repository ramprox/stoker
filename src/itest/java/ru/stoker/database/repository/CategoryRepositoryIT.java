package ru.stoker.database.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.stoker.annotations.RepositoryIT;
import ru.stoker.database.entity.Category;
import ru.stoker.util.DatabaseFacade;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Интеграционные тесты для CategoryRepository")
@RepositoryIT
public class CategoryRepositoryIT {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DatabaseFacade databaseFacade;

    @BeforeEach
    public void beforeEach() {
        databaseFacade.clean();
    }

    @DisplayName("Сохранение категории товара")
    @Test
    public void saveTest() {
        Category category = new Category("Category");

        categoryRepository.save(category);

        Optional<Category> actualCategoryOpt = categoryRepository.findById(1L);
        assertThat(actualCategoryOpt.isPresent()).isTrue();
    }

    @DisplayName("Сохранение дочерней категории")
    @Test
    public void saveChildTest() {
        Category parent = new Category("Category");
        categoryRepository.save(parent);

        parent = categoryRepository.getReferenceById(1L);
        Category child = new Category("Child", parent);
        categoryRepository.save(child);

        List<Category> actualChildren = categoryRepository.findByParentId(parent.getId());
        assertThat(actualChildren.size()).isEqualTo(1);
        Category actualChild = actualChildren.get(0);
        assertThat(actualChild).isNotNull();
        assertThat(actualChild.getId()).isEqualTo(2L);
        assertThat(actualChild.getParent().getId()).isEqualTo(1L);
    }

    @DisplayName("Извлечение корневых категорий")
    @Test
    public void findRootCategoriesTest() {
        List<Category> expectedRootCategories = categoryRepository.saveAll(List.of(
                new Category("Parent1"),
                new Category("Parent2")
        ));
        categoryRepository.save(new Category("Child1", expectedRootCategories.get(0)));
        categoryRepository.save(new Category("Child2", expectedRootCategories.get(1)));

        List<Category> actualRootCategories = categoryRepository.findByParentIsNull();

        assertThat(actualRootCategories).isEqualTo(expectedRootCategories);
    }

    @DisplayName("Ивлечение дочерних категорий по id родителя")
    @Test
    public void findByParentIdTest() {
        Category parent = categoryRepository.save(new Category("Parent"));
        List<Category> expectedChildren = categoryRepository.saveAll(
                List.of(new Category("Child1", parent),
                        new Category("Child2", parent),
                        new Category("Child3", parent)));

        List<Category> actualChildren = categoryRepository.findByParentId(parent.getId());
        assertThat(actualChildren).isEqualTo(expectedChildren);
    }

    @DisplayName("Удаление родительской категории вместе с дочерними категориями")
    @Test
    public void deleteParentWithChildrenTest() {
        Category parent = categoryRepository.save(new Category("Parent"));
        categoryRepository.saveAll(
                List.of(new Category("Child1", parent),
                        new Category("Child2", parent),
                        new Category("Child3", parent)));

        categoryRepository.deleteById(parent.getId());

        List<Category> actualChildren = categoryRepository.findByParentId(parent.getId());
        assertThat(actualChildren.size()).isEqualTo(0);
    }

}
