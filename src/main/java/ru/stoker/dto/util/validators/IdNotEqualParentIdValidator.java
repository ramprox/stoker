package ru.stoker.dto.util.validators;

import ru.stoker.dto.category.CategoryDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class IdNotEqualParentIdValidator implements
        ConstraintValidator<IdNotEqualParentId, CategoryDto> {
    @Override
    public boolean isValid(CategoryDto categoryDto, ConstraintValidatorContext constraintValidatorContext) {
        if(categoryDto == null || (categoryDto.getId() == null && categoryDto.getParentId() == null)) {
            return true;
        }
        return !Objects.equals(categoryDto.getId(), categoryDto.getParentId());
    }
}
