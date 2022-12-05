package ru.stoker.database.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.stoker.anotations.RepositoryIT;
import ru.stoker.database.entity.Category;
import ru.stoker.util.builder.DatabaseFacade;
import ru.stoker.util.factory.CategoryFactory;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.stoker.database.specification.CategorySpecification.parentId;
import static ru.stoker.util.factory.CategoryFactory.category;

@DisplayName("Интеграционные тесты для CategoryRepository")
@RepositoryIT
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private DatabaseFacade databaseFacade;

    @BeforeEach
    public void beforeEach() {
        databaseFacade.clean();
    }

    @DisplayName("Сохранение категории")
    @Test
    public void saveTest() {
        Category category = CategoryFactory.category("Category");

        categoryRepository.save(category);

        Optional<Category> actualCategoryOpt = categoryRepository.findById(1L);
        assertThat(actualCategoryOpt.isPresent()).isTrue();
    }

    @DisplayName("Сохранение дочерней категории")
    @Test
    public void saveChildTest() {
        Category parent = CategoryFactory.category("Category");
        categoryRepository.save(parent);

        parent = categoryRepository.getReferenceById(1L);
        Category child = new Category();
        child.setName("Child");
        child.setParent(parent);
        categoryRepository.save(child);

        List<Category> actualChildren = categoryRepository.findAll(parentId(parent.getId()));
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
                CategoryFactory.category("Parent1"),
                CategoryFactory.category("Parent2")
        ));
        categoryRepository.save(category("Child1", expectedRootCategories.get(0)));
        categoryRepository.save(category("Child2", expectedRootCategories.get(1)));

        List<Long> expectedRootCategoryIds = expectedRootCategories
                .stream().map(Category::getId).toList();
        List<Long> actualRootCategoryIds = categoryRepository.findAll(parentId(null))
                .stream().map(Category::getId).toList();

        assertThat(actualRootCategoryIds)
                .isEqualTo(expectedRootCategoryIds);
    }

    @DisplayName("Ивлечение дочерних категорий по id родителя")
    @Test
    public void findByParentIdTest() {
        Category parent = categoryRepository.save(CategoryFactory.category("Parent"));
        List<Category> expectedChildren = categoryRepository.saveAll(
                List.of(category("Child1", parent),
                        category("Child2", parent),
                        category("Child3", parent)));

        List<Long> expectedChildIds = expectedChildren
                .stream().map(Category::getId).toList();

        List<Long> actualChildIds = categoryRepository.findAll(parentId(parent.getId()))
                .stream().map(Category::getId).toList();

        assertThat(actualChildIds).isEqualTo(expectedChildIds);
    }

    @DisplayName("Удаление родительской категории вместе с дочерними категориями")
    @Test
    public void deleteParentWithChildrenTest() {
        Category parent = categoryRepository.save(CategoryFactory.category("Parent"));
        categoryRepository.saveAll(
                List.of(category("Child1", parent),
                        category("Child2", parent),
                        category("Child3", parent)));

        categoryRepository.deleteById(parent.getId());

        List<Category> actualChildren = categoryRepository.findAll(parentId(parent.getId()));
        assertThat(actualChildren.size()).isEqualTo(0);
    }

}
