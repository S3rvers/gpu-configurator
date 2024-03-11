package com.amalitech.gpuconfigurator.dto.product;

import com.amalitech.gpuconfigurator.util.ValidationErrorMessages;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    @NotNull(message = ValidationErrorMessages.PRODUCT_NAME_BLANK)
    @NotEmpty(message = ValidationErrorMessages.PRODUCT_NAME_EMPTY)
    private String productName;

    @NotNull(message = ValidationErrorMessages.PRODUCT_DESCRIPTION_BLANK)
    @NotEmpty(message = ValidationErrorMessages.PRODUCT_DESCRIPTION_EMPTY)
    private String productDescription;


    @NotNull(message = "Service charge cannot be null")
    @PositiveOrZero(message = "Service charge cannot be negative")
    private Double serviceCharge;

    @NotNull(message = ValidationErrorMessages.PRODUCT_PRICE_EMPTY)
    @NotEmpty(message = ValidationErrorMessages.PRODUCT_PRICE_NOT_NEGATIVE)
    private String productId;

    private String category;

    private String productCaseId;

    private Integer inStock;
}