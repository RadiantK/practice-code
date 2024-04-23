package com.jpashop.api.service.order.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class OrderCreateServiceRequest {

    private Long memberId;
    private Long productId;
    private int count;
    private LocalDateTime orderDate;

    @Builder
    public OrderCreateServiceRequest(Long memberId, Long productId, int count) {
        this.memberId = memberId;
        this.productId = productId;
        this.count = count;
        this.orderDate = LocalDateTime.now();
    }
}
