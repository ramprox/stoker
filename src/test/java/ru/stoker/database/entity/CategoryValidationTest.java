package ru.stoker.database.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Unit тесты валидации при создании категории")
public class CategoryValidationTest {

    private Validator validator;

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    @BeforeEach
    public void beforeEach() {
        validator = validatorFactory.getValidator();
    }

    @DisplayName("Создание категории без ошибок валидации")
    @ParameterizedTest
    @MethodSource("categories")
    public void createCategoryTest(Category category) {
        Set<ConstraintViolation<Category>> violations = validator.validate(category);
        assertThat(violations.size()).isEqualTo(0);
    }

    @DisplayName("Создание категории с недопустимыми именами")
    @ParameterizedTest
    @MethodSource("categoriesWithBlankName")
    public void createCategoryWithViolationsTest(Category category) {
        Set<ConstraintViolation<Category>> violations = validator.validate(category);
        assertThat(violations.size()).isEqualTo(1);
        ConstraintViolation<Category> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("Имя категории не должно быть пустым");
    }

    private static Stream<Category> categories() {
        return Stream.of(
                new Category("Category"),
                new Category("Category", null),
                new Category("Child", new Category("Parent"))
        );
    }

    private static Stream<Category> categoriesWithBlankName() {
        return Stream.of(
                new Category(null),
                new Category(""),
                new Category("   ")
        );
    }

}
