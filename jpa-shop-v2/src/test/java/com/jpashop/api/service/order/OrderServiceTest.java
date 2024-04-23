package com.jpashop.api.service.order;

import com.jpashop.api.controller.order.request.OrderCancelRequest;
import com.jpashop.api.controller.order.request.OrderCreateRequest;
import com.jpashop.api.service.order.request.OrderCreateServiceRequest;
import com.jpashop.domain.Address;
import com.jpashop.domain.member.Member;
import com.jpashop.domain.member.MemberRepository;
import com.jpashop.domain.order.Order;
import com.jpashop.domain.order.OrderRepository;
import com.jpashop.domain.order.OrderStatus;
import com.jpashop.domain.orderItem.OrderItem;
import com.jpashop.domain.orderItem.OrderItemRepository;
import com.jpashop.domain.product.Product;
import com.jpashop.domain.product.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.jpashop.domain.product.SellingStatus.SELLING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("새로운 주문을 등록한다.")
    @Test
    void createOrder() {
        // given
        Product product = createProduct("노트");
        productRepository.save(product);

        Member member = Member.builder().name("kang").address(new Address("seoul", "guro", "123")).build();
        memberRepository.save(member);

        OrderCreateRequest request = OrderCreateRequest.builder()
                .memberId(member.getId())
                .productId(product.getId())
                .count(5)
                .build();

        OrderCreateServiceRequest serviceRequest = request.toServiceRequest();

        // when
        OrderCreateResponse response = orderService.createOrder(serviceRequest);

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getOrderDate()).isEqualTo(serviceRequest.getOrderDate());
        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.ORDERED);
    }

    @DisplayName("새로운 주문을 등록할 때 존재하지 않는 회원번호는 예외가 발생한다.")
    @Test
    void createOrderWithoutMemberId() {
        // given
        Product product = createProduct("노트");
        productRepository.save(product);

        Member member = Member.builder().name("kang").address(new Address("seoul", "guro", "123")).build();
        memberRepository.save(member);

        OrderCreateRequest request = OrderCreateRequest.builder()
                .memberId(member.getId() + 1)
                .productId(product.getId())
                .count(5)
                .build();

        OrderCreateServiceRequest serviceRequest = request.toServiceRequest();

        // when & then
        assertThatThrownBy(() -> orderService.createOrder(serviceRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("회원정보를 확인하세요.");
    }

    @DisplayName("새로운 주문을 등록할 때 존재하지 않는 상품 번호는 예외가 발생한다.")
    @Test
    void createOrderWithoutProductId() {
        // given
        Product product = createProduct("노트");
        productRepository.save(product);

        Member member = Member.builder().name("kang").address(new Address("seoul", "guro", "123")).build();
        memberRepository.save(member);

        OrderCreateRequest request = OrderCreateRequest.builder()
                .memberId(member.getId())
                .productId(product.getId() + 1)
                .count(5)
                .build();

        OrderCreateServiceRequest serviceRequest = request.toServiceRequest();

        // when & then
        assertThatThrownBy(() -> orderService.createOrder(serviceRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상품정보를 확인하세요.");
    }

    @DisplayName("새로운 주문을 등록할 때 상품 재고보다 높은 수량을 주문 시 예외가 발생한다.")
    @Test
    void createOrderStockQuantityOver() {
        // given
        Product product = createProduct("노트");
        productRepository.save(product);

        Member member = Member.builder().name("kang").address(new Address("seoul", "guro", "123")).build();
        memberRepository.save(member);

        OrderCreateRequest request = OrderCreateRequest.builder()
                .memberId(member.getId())
                .productId(product.getId())
                .count(11)
                .build();

        OrderCreateServiceRequest serviceRequest = request.toServiceRequest();

        // when & then
        assertThatThrownBy(() -> orderService.createOrder(serviceRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("재고 수량이 부족합니다.");
    }

    @DisplayName("주문을 취소한다.")
    @Test
    void cancelOrder() {
        // given
        Product product = createProduct("노트");
        productRepository.save(product);

        Member member = Member.builder().name("kang").address(new Address("seoul", "guro", "123")).build();
        memberRepository.save(member);

        OrderCreateRequest request = OrderCreateRequest.builder()
                .memberId(member.getId())
                .productId(product.getId())
                .count(5)
                .build();

        OrderCreateServiceRequest serviceRequest = request.toServiceRequest();
        OrderCreateResponse response = orderService.createOrder(serviceRequest);

        OrderCancelRequest cancelRequest = OrderCancelRequest.builder()
                .orderId(response.getId())
                .build();

        // when
        orderService.cancelOrder(cancelRequest.toServiceRequest());

        // then
        assertThat(product.getStockQuantity()).isEqualTo(10);

        Order findOrder = orderRepository.findById(response.getId()).get();
        assertThat(findOrder.getOrderStatus()).isEqualTo(OrderStatus.CANCELED);
    }

    @DisplayName("주문을 취소할 때 잘못된 주문번호이면 예외가 발생한다.")
    @Test
    void cancelOrderWithoutOrderId() {
        // given
        Product product = createProduct("노트");
        productRepository.save(product);

        Member member = Member.builder().name("kang").address(new Address("seoul", "guro", "123")).build();
        memberRepository.save(member);

        OrderCreateRequest request = OrderCreateRequest.builder()
                .memberId(member.getId())
                .productId(product.getId())
                .count(5)
                .build();

        OrderCreateServiceRequest serviceRequest = request.toServiceRequest();
        OrderCreateResponse response = orderService.createOrder(serviceRequest);

        OrderCancelRequest cancelRequest = OrderCancelRequest.builder()
                .orderId(response.getId() + 1)
                .build();

        // when & then
        assertThatThrownBy(() -> orderService.cancelOrder(cancelRequest.toServiceRequest()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문번호 확인하세요.");
    }

    private Product createProduct(String name) {
        return Product.builder()
                .name(name)
                .price(5000)
                .stockQuantity(10)
                .sellingStatus(SELLING)
                .build();
    }
}