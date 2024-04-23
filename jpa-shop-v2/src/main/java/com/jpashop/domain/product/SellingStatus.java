package com.jpashop.domain.product;

import lombok.Getter;

import java.util.List;

@Getter
public enum SellingStatus {

    SELLING("판매중"),
    HOLD("판매보류"),
    STOP_SELLING("판매종료")
    ;

    SellingStatus(String desc) {
        this.desc = desc;
    }

    private final String desc;

    public static boolean isSelling(SellingStatus sellingStatus) {
        return SELLING == sellingStatus;
    }
}
