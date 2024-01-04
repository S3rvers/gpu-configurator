package com.amalitech.gpuconfigurator.dto;

import com.amalitech.gpuconfigurator.util.PasswordMinimumLength;
import com.amalitech.gpuconfigurator.util.ValidationErrorMessages;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @NotBlank(message = ValidationErrorMessages.FIRST_NAME_BLANK)
    private String firstName;

    @NotBlank(message = ValidationErrorMessages.LAST_NAME_BLANK)
    private String lastName;

    @NotBlank(message = ValidationErrorMessages.EMAIL_BLANK)
    @Email(message = ValidationErrorMessages.INVALID_EMAIL_FORMAT)
    private String email;

    @NotBlank(message = ValidationErrorMessages.PASSWORD_BLANK)
    @Size(min = PasswordMinimumLength.MIN_LENGTH, message = ValidationErrorMessages.PASSWORD_MIN_LENGTH)
    private String password;
}