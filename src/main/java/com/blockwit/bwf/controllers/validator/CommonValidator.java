package com.blockwit.bwf.controllers.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.ConstraintViolation;
import java.util.Set;

public abstract class CommonValidator implements Validator {

    private final javax.validation.Validator javaxValidator;

    private final Class clazz;

    public CommonValidator(javax.validation.Validator javaxValidator, Class clazz) {
        this.javaxValidator = javaxValidator;
        this.clazz = clazz;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return clazz.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Set<ConstraintViolation<Object>> validates = javaxValidator.validate(o);
        for (ConstraintViolation<Object> constraintViolation : validates) {
            String propertyPath = constraintViolation.getPropertyPath().toString();
            String message = constraintViolation.getMessage();
            errors.rejectValue(propertyPath, "", message);
        }
        performValidate(o, errors);
    }

    public void performValidate(Object o, Errors errors) {}

}
