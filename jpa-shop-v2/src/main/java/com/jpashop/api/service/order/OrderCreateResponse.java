package com.jpashop.api.service.order;

import com.jpashop.domain.order.Order;
import com.jpashop.domain.order.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class OrderCreateResponse {

    private Long id;
    private OrderStatus orderStatus;
    private LocalDateTime orderDate;

    @Builder
    public OrderCreateResponse(Long id, OrderStatus orderStatus, LocalDateTime orderDate) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
    }

    public static OrderCreateResponse of(Order order) {
        return OrderCreateResponse.builder()
                .id(order.getId())
                .orderStatus(order.getOrderStatus())
                .orderDate(order.getOrderDate())
                .build();
    }
}
