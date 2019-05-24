package com.mymoneyapp.backend.validator.impl;

import com.mymoneyapp.backend.validator.Alphabetic;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AlphabeticImpl implements ConstraintValidator<Alphabetic, String> {

    @Override
    @SuppressWarnings("PMD")
    public void initialize(final Alphabetic alphabetic) {
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext cxt) {
        return value != null
                && !value.isEmpty()
                && value.matches("^[a-zA-ZÀ-ÿ ]+$");
    }

}
