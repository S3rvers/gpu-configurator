package com.amalitech.gpuconfigurator.dto;

import com.amalitech.gpuconfigurator.util.ValidationErrorMessages;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyOtpDTO {
    @NotBlank(message = ValidationErrorMessages.EMAIL_BLANK)
    @Email(message = ValidationErrorMessages.INVALID_EMAIL_FORMAT)
    public String email;

    @NotBlank(message = ValidationErrorMessages.OTP_BLANK)
    public String otpCode;
}
