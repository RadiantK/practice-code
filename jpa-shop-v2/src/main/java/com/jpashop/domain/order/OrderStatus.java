package com.jpashop.domain.order;

import lombok.Getter;

@Getter
public enum OrderStatus {

    ORDERED("주문"),
    CANCELED("주문취소"),
    COMPLETED("처리완료")
    ;

    OrderStatus(String desc) {
        this.desc = desc;
    }

    private final String desc;
}
