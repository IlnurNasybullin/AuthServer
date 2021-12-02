package org.freestudy.authserver.validator;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LoginValidator implements ConstraintValidator<Login, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !StringUtils.isEmpty(value) && value.trim().length() == value.length();
    }
}
