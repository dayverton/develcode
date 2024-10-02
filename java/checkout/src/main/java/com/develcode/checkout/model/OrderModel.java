package com.develcode.checkout.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "order")
public class OrderModel {

    @Id
    @Schema(type = "string", required = true, maxLength = 24)
    private String id;

    @Schema(type = "double", required = true)
    private Double amount;

    @Schema(type = "string", required = true, maxLength = 30)
    private String status;

}
