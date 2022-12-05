package ru.stoker.dto.util.validators;

import ru.stoker.dto.product.productproperties.apartment.ApartmentAreaDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TotalAreaValidator implements ConstraintValidator<TotalAreaValid, ApartmentAreaDto> {

    @Override
    public boolean isValid(ApartmentAreaDto apartmentArea, ConstraintValidatorContext constraintValidatorContext) {
        return !(apartmentArea.getKitchenArea() +
                apartmentArea.getLivingArea() > apartmentArea.getTotalArea());
    }

}
