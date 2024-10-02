package com.develcode.checkout.enums;

import lombok.Getter;

public enum CheckoutStatusEnum {
    AWAITING("AWAITING"),
    PAID("PAID"),
    CANCELLED("CANCELLED");

    @Getter
    private String value;

    CheckoutStatusEnum(String value) {
        this.value = value;
    }

}
