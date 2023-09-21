package com.example.usersdb.validAnnotation;

import com.example.usersdb.validators.SumOfCostsLessThen1000Validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SumOfCostsLessThen1000Validator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SumOfCostsLessThen1000 {
    String message() default "Sum of costs already greater than 1000";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
