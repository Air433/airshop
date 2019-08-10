package com.airshop.order.dto;

/**
 * @Author ouyanggang
 * @Date 2019/8/10 - 21:06
 */
public enum PayStateEnum {

    NOT_PAY(0), SUCCESS(1), FAIL(2);

    int value;

    PayStateEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
