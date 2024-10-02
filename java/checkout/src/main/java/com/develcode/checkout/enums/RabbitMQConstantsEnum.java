package com.develcode.checkout.enums;

import lombok.Getter;

public enum RabbitMQConstantsEnum {
    CHECKOUT_EXCHANGE("checkout_exchange"),
    ORDER_CHECKOUT_QUEUE("order_checkout_queue"),
    PAYMENT_STATUS_QUEUE("payment_status_queue"),
    CHECKOUT_ROUTING_KEY("checkout_routing_key"),    
    PAYMENT_STATUS_ROUTING_KEY("payment_status_routing_key"),
    ;

    @Getter
    private final String value;

    RabbitMQConstantsEnum(String value) {
        this.value = value;
    }
}
