package com.amalitech.gpuconfigurator.dto;

import com.amalitech.gpuconfigurator.util.ValidationErrorMessages;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordDTO {
    @NotBlank(message = ValidationErrorMessages.EMAIL_BLANK)
    @Email(message = ValidationErrorMessages.MUST_BE_EMAIL)
    public String email;
}
