package com.amalitech.gpuconfigurator.dto.profile;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ContactRequest {
    @NotBlank
    public String phoneNumber;

    @NotBlank
    public String country;

    @NotBlank
    public String iso2Code;

    @NotBlank
    public String dialCode;
}