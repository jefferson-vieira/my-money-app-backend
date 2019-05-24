package com.mymoneyapp.backend.validator.impl;

import com.mymoneyapp.backend.validator.Alphabetic;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AlphabeticImpl implements ConstraintValidator<Alphabetic, String> {

    @Override
    public void initialize(Alphabetic alphabetic) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext cxt) {
        return value != null
                && !value.isEmpty()
                && value.matches("^[a-zA-ZÀ-ÿ ]+$");
    }

}
