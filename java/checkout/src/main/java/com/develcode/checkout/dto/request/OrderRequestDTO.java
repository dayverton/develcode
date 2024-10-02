package com.develcode.checkout.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record OrderRequestDTO(
        @NotNull(message = "O valor não pode ser nulo.") @DecimalMin(value = "0.0", inclusive = false, message = "O valor deve ser maior que 0") Double amount) {

}
