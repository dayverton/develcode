package com.develcode.checkout.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record OrderEventRequestDTO(@DecimalMin(value = "0.0") Double amount, @NotBlank String id) {
}
