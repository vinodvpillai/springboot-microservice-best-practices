package com.vinod.microservices.best.practices.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class AddressValidator implements ConstraintValidator<AddressValidation,String> {

    List<String> addresses = Arrays.asList("Gujarat","Pune","Mumbai");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        //Add the custom validation here.
        return addresses.contains(value);
    }
}
