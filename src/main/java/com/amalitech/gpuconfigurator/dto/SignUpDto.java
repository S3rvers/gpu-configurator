package com.amalitech.gpuconfigurator.dto;

import com.amalitech.gpuconfigurator.util.PasswordMinimumLength;
import com.amalitech.gpuconfigurator.util.ValidationErrorMessages;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpDto {

    @NotBlank(message = ValidationErrorMessages.FIRST_NAME_BLANK)
    @NotNull(message = ValidationErrorMessages.FIRST_NAME_NULL)
    private String firstName;

    @NotBlank(message = ValidationErrorMessages.LAST_NAME_BLANK)
    @NotNull(message = ValidationErrorMessages.LAST_NAME_NULL)
    private String lastName;

    @Email(message = ValidationErrorMessages.MUST_BE_EMAIL)
    @NotBlank(message = ValidationErrorMessages.EMAIL_BLANK)
    @NotNull(message = ValidationErrorMessages.EMAIL_NULL)
    private String email;

    @Size(min = PasswordMinimumLength.MIN_LENGTH, message = ValidationErrorMessages.PASSWORD_MIN_LENGTH)
    private String password;
}
