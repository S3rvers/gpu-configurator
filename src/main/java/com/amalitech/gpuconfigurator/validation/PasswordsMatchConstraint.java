package com.amalitech.gpuconfigurator.validation;

import com.amalitech.gpuconfigurator.annotation.PasswordsMatch;
import com.amalitech.gpuconfigurator.dto.ChangePasswordDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordsMatchConstraint implements ConstraintValidator<PasswordsMatch, Object> {
    @Override
    public void initialize(PasswordsMatch passwordsMatch) {
        ConstraintValidator.super.initialize(passwordsMatch);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value instanceof ChangePasswordDTO dto) {
            return dto.getNewPassword().equals(dto.getConfirmNewPassword());
        }
        return false;
    }
}
