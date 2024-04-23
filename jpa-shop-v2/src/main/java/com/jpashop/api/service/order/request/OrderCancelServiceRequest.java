package com.jpashop.api.service.order.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderCancelServiceRequest {

    private Long orderId;

    @Builder
    public OrderCancelServiceRequest(Long orderId) {
        this.orderId = orderId;
    }
}
