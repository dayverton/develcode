package com.develcode.checkout.dto.response;

import lombok.Data;

@Data
public class PaymentConfirmationResponseDTO {

    private String orderId;
    private boolean success;

}
