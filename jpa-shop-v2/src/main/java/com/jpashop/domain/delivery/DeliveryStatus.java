package com.jpashop.domain.delivery;

import lombok.Getter;

@Getter
public enum DeliveryStatus {

    READY("배종준비"),
    PROCESSING("배송중"),
    COMPLETE("배송완료"),
    ;


    DeliveryStatus(String desc) {
        this.desc = desc;
    }

    private final String desc;
}
