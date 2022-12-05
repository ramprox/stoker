package ru.stoker.controller.admin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import ru.stoker.controller.BaseControllerTest;
import ru.stoker.dto.category.CategoryDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.RequestEntity.*;
import static ru.stoker.util.factory.CategoryDtoFactory.categoryDto;

@DisplayName("Интеграционные тесты CategoryController")
public class CategoryControllerTest extends BaseControllerTest {

    @DisplayName("Сохранение корневой категории")
    @Test
    public void saveTest() {
        CategoryDto expectedCategory = categoryDto("Category");

        ResponseEntity<CategoryDto> response = saveCategory(expectedCategory, CategoryDto.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        expectedCategory.setId(1L);
        CategoryDto actualCategory = response.getBody();
        assertThat(actualCategory).isEqualTo(expectedCategory);
    }

    @DisplayName("Сохранение дочерней категории")
    @Test
    public void saveChildTest() {
        CategoryDto parent = saveCategoryAndGet(categoryDto("Category"));

        CategoryDto expectedCategory = categoryDto("Child", parent.getId());
        ResponseEntity<CategoryDto> response = saveCategory(expectedCategory, CategoryDto.class);

        expectedCategory.setId(2L);
        CategoryDto actualCategory = response.getBody();
        assertThat(actualCategory).isEqualTo(expectedCategory);
    }

    @DisplayName("Извлечение категории по id")
    @Test
    public void getByIdTest() {
        CategoryDto expectedCategory = saveCategoryAndGet(categoryDto("Category"));

        ResponseEntity<CategoryDto> response = getCategoryById(expectedCategory.getId(), CategoryDto.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        CategoryDto actualCategory = response.getBody();
        assertThat(actualCategory).isEqualTo(expectedCategory);
    }

    @DisplayName("Обновление корневой категории")
    @Test
    public void updateTest() {
        CategoryDto expectedCategory = saveCategoryAndGet(categoryDto("Category"));

        String newCategoryName = "newCategoryName";
        expectedCategory.setName(newCategoryName);
        ResponseEntity<CategoryDto> response = updateCategory(expectedCategory, CategoryDto.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        CategoryDto actualCategory = response.getBody();
        assertThat(actualCategory).isEqualTo(expectedCategory);
    }

    @DisplayName("Обновление дочерней категории")
    @Test
    public void updateChildTest() {
        CategoryDto parent = saveCategoryAndGet(categoryDto("Category"));
        CategoryDto child = categoryDto("Child", parent.getId());
        ResponseEntity<CategoryDto> response = saveCategory(child, CategoryDto.class);
        child = response.getBody();
        child.setName("newChildName");

        response = updateCategory(child, CategoryDto.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        CategoryDto actualCategory = response.getBody();
        assertThat(actualCategory).isEqualTo(child);
    }

    @DisplayName("Удаление категории по id")
    @Test
    public void deleteByIdTest() {
        CategoryDto categoryDto = saveCategoryAndGet(categoryDto("Category"));
        Long id = categoryDto.getId();

        ResponseEntity<Void> response = deleteById(id, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        ResponseEntity<String> responseAfterDelete = getCategoryById(id, String.class);
        assertThat(responseAfterDelete.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @DisplayName("Удаление несуществующей категории")
    @Test
    public void deleteNotExistCategoryByIdTest() {
        ResponseEntity<String> response = deleteById(1L, String.class);

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
        String actualBody = response.getBody();
        assertThat(actualBody).isEqualTo("Категория с id = 1 не найдена");
    }

    @DisplayName("Сохранение с неправильными данными")
    @Test
    public void bindExceptionTest() {
        CategoryDto category = categoryDto("");
        List<String> expectedErrors =
                List.of("Имя категории не должно быть пустым");

        ResponseEntity<List<String>> response = saveCategory(category, new ParameterizedTypeReference<>() { });
        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        List<String> actualBody = response.getBody();
        assertThat(actualBody).isEqualTo(expectedErrors);
    }

    @DisplayName("Сохранение корневой категории с уже существующим именем")
    @Test
    public void saveCategoryWithDuplicateRootNameTest() {
        CategoryDto category = saveCategoryAndGet(categoryDto("Category"));
        category = categoryDto(category.getName(), category.getParentId());

        ResponseEntity<String> response = saveCategory(category, String.class);

        assertThat(response.getStatusCode()).isEqualTo(CONFLICT);
        String actualBody = response.getBody();
        String expectedMessage = String.format("Корневая категория с именем '%s' уже существует", category.getName());
        assertThat(actualBody).isEqualTo(expectedMessage);
    }

    @DisplayName("Изменение имени одного из корневых категории при условии, что имя уже существует")
    @Test
    public void updateRootCategoryWithExistingNameTest() {
        saveCategoryAndGet(categoryDto("Category1"));
        CategoryDto category = saveCategoryAndGet(categoryDto("Category2"));
        category.setName("Category1");

        ResponseEntity<String> response = updateCategory(category, String.class);

        assertThat(response.getStatusCode()).isEqualTo(CONFLICT);
        String actualBody = response.getBody();
        String expectedMessage = String.format("Корневая категория с именем '%s' уже существует", category.getName());
        assertThat(actualBody).isEqualTo(expectedMessage);
    }

    @DisplayName("Изменение parentId одного из корневых категории при условии, что такой parentId не существует")
    @Test
    public void updateRootCategoryWithNonExistingParentCategory() {
        saveCategoryAndGet(categoryDto("Category1"));
        CategoryDto category = saveCategoryAndGet(categoryDto("Category2"));
        category.setParentId(3L);

        ResponseEntity<String> response = updateCategory(category, String.class);

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
        String actualBody = response.getBody();
        String expectedMessage = String.format("Родительская категория с id = %d не существует", category.getParentId());
        assertThat(actualBody).isEqualTo(expectedMessage);
    }

    @DisplayName("Сохранение дочерней категории с уже существующим именем и одинаковым parentId")
    @Test
    public void saveCategoryWithDuplicateNameTest() {
        CategoryDto parent = saveCategoryAndGet(categoryDto("Category"));
        CategoryDto child = categoryDto("Child", parent.getId());
        saveCategory(child, CategoryDto.class);

        ResponseEntity<String> response = saveCategory(child, String.class);

        String expectedMessage = "Категория с именем 'Child' и родительским id = 1 уже существует";
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        String actualBody = response.getBody();
        assertThat(actualBody).isEqualTo(expectedMessage);
    }

    private <T> ResponseEntity<T> getCategoryById(Long id, Class<T> type) {
        RequestEntity<Void> request = get("/api/v1/category/{id}", id)
                .header(AUTHORIZATION, adminAuthHeader())
                .build();
        return restTemplate.exchange(request, type);
    }

    private <T> ResponseEntity<T> updateCategory(CategoryDto category, Class<T> type) {
        RequestEntity<CategoryDto> request = put("/api/v1/category")
                .header(AUTHORIZATION, adminAuthHeader())
                .body(category);
        return restTemplate.exchange(request, type);
    }

    private <T> ResponseEntity<T> deleteById(Long id, Class<T> type) {
        RequestEntity<Void> request = delete("/api/v1/category/{id}", id)
                .header(AUTHORIZATION, adminAuthHeader())
                .build();
        return restTemplate.exchange(request, type);
    }

}
