package com.mymoneyapp.backend.validator;

import com.mymoneyapp.backend.validator.impl.AlphabeticImpl;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {AlphabeticImpl.class})
public @interface Alphabetic {

    String message() default "Only alphabetic characters";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
