package com.vinod.microservices.best.practices.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = AddressValidator.class)
public @interface AddressValidation {

    String message() default "Please enter a valid address.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
