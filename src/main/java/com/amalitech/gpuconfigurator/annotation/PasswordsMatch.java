package com.amalitech.gpuconfigurator.annotation;

import com.amalitech.gpuconfigurator.validation.PasswordsMatchConstraint;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Constraint(validatedBy = PasswordsMatchConstraint.class)
public @interface PasswordsMatch {
    String message() default "Passwords do not match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
