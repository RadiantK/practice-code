package com.jpashop.api.controller.order;

import com.jpashop.api.ApiResponse;
import com.jpashop.api.controller.order.request.OrderCancelRequest;
import com.jpashop.api.controller.order.request.OrderCreateRequest;
import com.jpashop.api.service.order.OrderCreateResponse;
import com.jpashop.api.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders/new")
    public ApiResponse<OrderCreateResponse> newOrder(@Validated @RequestBody OrderCreateRequest request) {
        return ApiResponse.of(orderService.createOrder(request.toServiceRequest()));
    }

    @PostMapping("/orders/cancel")
    public ApiResponse<Boolean> cancelOrder(@Validated @RequestBody OrderCancelRequest request) {

        orderService.cancelOrder(request.toServiceRequest());

        return ApiResponse.of(true);
    }
}
