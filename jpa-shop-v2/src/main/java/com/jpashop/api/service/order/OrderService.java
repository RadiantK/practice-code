package com.jpashop.api.service.order;

import com.jpashop.api.service.order.request.OrderCancelServiceRequest;
import com.jpashop.api.service.order.request.OrderCreateServiceRequest;
import com.jpashop.domain.delivery.Delivery;
import com.jpashop.domain.delivery.DeliveryStatus;
import com.jpashop.domain.member.Member;
import com.jpashop.domain.member.MemberRepository;
import com.jpashop.domain.order.Order;
import com.jpashop.domain.order.OrderRepository;
import com.jpashop.domain.orderItem.OrderItem;
import com.jpashop.domain.product.Product;
import com.jpashop.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public OrderCreateResponse createOrder(OrderCreateServiceRequest request) {
        Member findMember = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("회원정보를 확인하세요."));

        Product findProduct = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품정보를 확인하세요."));

        Delivery delivery = Delivery.builder()
                .status(DeliveryStatus.READY)
                .address(findMember.getAddress())
                .build();

        OrderItem orderItem = OrderItem.createOrderItem(findProduct, request.getCount());

        Order order = Order.createOrder(findMember, delivery, request.getOrderDate(), orderItem);
        orderRepository.save(order);

        return OrderCreateResponse.of(order);
    }

    @Transactional
    public void cancelOrder(OrderCancelServiceRequest request) {
        Order findOrder = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("주문번호 확인하세요."));

        // 주문 취소
        findOrder.cancel();
    }
}
