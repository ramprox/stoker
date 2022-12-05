package ru.stoker.dto.category;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.stoker.dto.util.validgroups.OnCreate;
import ru.stoker.dto.util.validgroups.OnUpdate;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.stoker.util.factory.CategoryDtoFactory.categoryDto;

@DisplayName("Unit тесты валидации при создании категории")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CategoryDtoTest {

    private Validator validator;

    @BeforeAll
    public void beforeAll() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @DisplayName("Создание категории без ошибок валидации")
    @ParameterizedTest
    @MethodSource("categories")
    public void validateCorrectCategoryTest(CategoryDto category) {
        Set<ConstraintViolation<CategoryDto>> violations = validator.validate(category);
        assertThat(violations.size()).isEqualTo(0);
    }

    @DisplayName("Создание категории с недопустимыми именами")
    @ParameterizedTest
    @MethodSource("categoriesWithBlankName")
    public void validateCategoryWithIncorrectNamesTest(CategoryDto category) {
        Set<ConstraintViolation<CategoryDto>> violations = validator.validate(category);
        assertThat(violations.size()).isEqualTo(1);
        ConstraintViolation<CategoryDto> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("{category.name.not.blank}");
    }

    @DisplayName("Категория c id = null на создание д.б. без ошибок")
    @Test
    public void validateCategoryOnCreateTest() {
        CategoryDto category = categoryDto("Category");
        Set<ConstraintViolation<CategoryDto>> violations = validator.validate(category, OnCreate.class);
        assertThat(violations.size()).isEqualTo(0);
    }

    @DisplayName("Категория c id != null на создание д.б. с ошибкой")
    @Test
    public void validateCategoryWithIdNotNullOnCreateTest() {
        CategoryDto category = categoryDto("Category");
        category.setId(1L);
        Set<ConstraintViolation<CategoryDto>> violations = validator.validate(category, OnCreate.class);
        ConstraintViolation<CategoryDto> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("{category.id.null}");
    }

    @DisplayName("Категория c id != null на обновление д.б. без ошибок")
    @Test
    public void validateCategoryOnUpdateTest() {
        CategoryDto category = categoryDto("Category");
        category.setId(1L);
        Set<ConstraintViolation<CategoryDto>> violations = validator.validate(category, OnUpdate.class);
        assertThat(violations.size()).isEqualTo(0);
    }

    @DisplayName("Категория c id = null на обновление д.б. с ошибкой")
    @Test
    public void validateCategoryWithIdNotNullOnUpdateTest() {
        CategoryDto category = categoryDto("Category");
        Set<ConstraintViolation<CategoryDto>> violations = validator.validate(category, OnUpdate.class);
        ConstraintViolation<CategoryDto> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("{category.id.not.null}");
    }

    private static Stream<CategoryDto> categories() {
        return Stream.of(
                create("Category"),
                create("Child", 1L)
        );
    }

    private static Stream<CategoryDto> categoriesWithBlankName() {
        return Stream.of(
                create(null),
                create(""),
                create("   ")
        );
    }

    private static CategoryDto create(String name) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(name);
        return categoryDto;
    }

    private static CategoryDto create(String name, Long parentId) {
        CategoryDto categoryDto = create(name);
        categoryDto.setParentId(parentId);
        return categoryDto;
    }

}
