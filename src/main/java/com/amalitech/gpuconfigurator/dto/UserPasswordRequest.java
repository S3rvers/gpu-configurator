package com.amalitech.gpuconfigurator.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserPasswordRequest {
    @NotBlank
    public String currentPassword;

    @NotBlank
    public String newPassword;

    @NotBlank
    public String confirmNewPassword;
}
