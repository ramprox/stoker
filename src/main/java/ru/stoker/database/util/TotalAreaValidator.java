package ru.stoker.database.util;

import ru.stoker.database.entity.productproperties.ApartmentArea;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TotalAreaValidator implements ConstraintValidator<TotalAreaValid, ApartmentArea> {

    @Override
    public boolean isValid(ApartmentArea apartmentArea, ConstraintValidatorContext constraintValidatorContext) {
        return !(apartmentArea.getKitchenArea() +
                apartmentArea.getLivingArea() > apartmentArea.getTotalArea());
    }

}
