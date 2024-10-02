package com.develcode.checkout.dto.response;

import lombok.Data;

@Data
public class OrderResponseDTO {

    private String id;

    private Double amount;

    private String status;

}
