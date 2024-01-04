package com.amalitech.gpuconfigurator.dto;

import com.amalitech.gpuconfigurator.annotation.PasswordsMatch;
import com.amalitech.gpuconfigurator.util.ValidationErrorMessages;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@PasswordsMatch(message = "password and confirm password are not equal")
public class ChangePasswordDTO {


    @NotBlank(message = ValidationErrorMessages.EMAIL_BLANK)
    @Email(message = ValidationErrorMessages.MUST_BE_EMAIL)
    public String email;

    @NotBlank(message = ValidationErrorMessages.OTP_BLANK)
    public String otpCode;

    @NotBlank(message = ValidationErrorMessages.NEW_PASSWORD_NOT_BLANK)
    public String newPassword;

    @NotBlank(message = ValidationErrorMessages.CONFIRM_PASSWORD_NOT_BLANK)
    public String confirmNewPassword;
}
