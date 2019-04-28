package com.mymoneyapp.backend.validator.impl;

import com.mymoneyapp.backend.validator.Password;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordImpl implements ConstraintValidator<Password, String> {

    @Override
    public void initialize(Password password) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext cxt) {
        return value != null
                && value.matches("^[^\\s]+$")
                && value.matches("\\W+")
                && value.matches("\\d+")
                && value.matches("[^\\W\\d]+")
                && (value.length() > 7)
                && (value.length() < 17);
    }

}
