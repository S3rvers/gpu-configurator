package com.amalitech.gpuconfigurator.dto.category;


import com.amalitech.gpuconfigurator.util.ValidationErrorMessages;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllCategoryResponse {
    private String id;

    @NotNull(message = ValidationErrorMessages.CATEGORY_NAME)
    private String name;
}
