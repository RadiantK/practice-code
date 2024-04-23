package com.jpashop.api.controller.order.request;

import com.jpashop.api.service.order.request.OrderCancelServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class OrderCancelRequest {

    @NotNull(message = "주문 번호는 필수입니다.")
    private Long orderId;

    @Builder
    public OrderCancelRequest(Long orderId) {
        this.orderId = orderId;
    }

    public OrderCancelServiceRequest toServiceRequest() {
        return OrderCancelServiceRequest.builder()
                .orderId(orderId)
                .build();
    }
}
