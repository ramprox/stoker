package ru.stoker.database.util;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.FIELD })
@Constraint(validatedBy = TotalAreaValidator.class)
public @interface TotalAreaValid {

    String message() default "total area must be valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
