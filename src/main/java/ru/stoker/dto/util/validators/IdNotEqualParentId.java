package ru.stoker.dto.util.validators;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD })
@Constraint(validatedBy = IdNotEqualParentIdValidator.class)
public @interface IdNotEqualParentId {

    String message() default "Id must not be equals to parentId";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
