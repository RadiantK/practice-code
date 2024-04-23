package com.jpashop.api.controller.order.request;

import com.jpashop.api.service.order.request.OrderCreateServiceRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor
public class OrderCreateRequest {

    @NotNull(message = "회원 아이디는 필수입니다.")
    private Long memberId;
    @NotNull(message = "상품 아이디는 필수입니다.")
    private Long productId;
    @Positive(message = "상품수량은 양수만 가능합니다.")
    private int count;

    @Builder
    public OrderCreateRequest(Long memberId, Long productId, int count) {
        this.memberId = memberId;
        this.productId = productId;
        this.count = count;
    }

    public OrderCreateServiceRequest toServiceRequest() {
        return OrderCreateServiceRequest.builder()
                .memberId(memberId)
                .productId(productId)
                .count(count)
                .build();
    }
}
