package com.jpashop.domain.order;

import com.jpashop.domain.delivery.Delivery;
import com.jpashop.domain.member.Member;
import com.jpashop.domain.member.MemberRepository;
import com.jpashop.domain.orderItem.OrderItem;
import com.jpashop.domain.orderItem.OrderItemRepository;
import com.jpashop.domain.product.Product;
import com.jpashop.domain.product.ProductRepository;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static com.jpashop.domain.order.OrderStatus.COMPLETED;
import static com.jpashop.domain.order.OrderStatus.ORDERED;
import static com.jpashop.domain.product.SellingStatus.SELLING;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        orderItemRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("전체 주문 목록을 출력한다.")
    @Test
    void findAll() {
        // given
        Product product1 = createProduct("노트");
        Product product2 = createProduct("볼펜");
        productRepository.saveAll(List.of(product1, product2));
        System.out.println("product1.getId() = " + product1.getId());
        System.out.println("product2.getId() = " + product2.getId());

        Member member = Member.builder().build();
        memberRepository.save(member);

        LocalDateTime currDateTime = LocalDateTime.now();
        OrderItem orderItem1 = createOrderItem(product1);
        OrderItem orderItem2 = createOrderItem(product2);

        Order order1 = createOrder(member, orderItem1, currDateTime, ORDERED);
        Order order2 = createOrder(member, orderItem2, currDateTime, COMPLETED);
        orderRepository.saveAll(List.of(order1, order2));

        // when
        List<Order> findOrders = orderRepository.findAll();

        // then
        assertThat(findOrders).hasSize(2)
                .extracting("orderStatus", "orderDate")
                .containsExactlyInAnyOrder(
                        Tuple.tuple(ORDERED, currDateTime),
                        Tuple.tuple(COMPLETED, currDateTime)
                );
    }

    @DisplayName("전체 주문 목록을 출력한다.")
    @Test
    void findAll2() {
        // given
        Product product1 = createProduct("노트");
        Product product2 = createProduct("볼펜");
        productRepository.saveAll(List.of(product1, product2));
        System.out.println("product1.getId() = " + product1.getId());
        System.out.println("product2.getId() = " + product2.getId());

        Member member = Member.builder().build();
        memberRepository.save(member);

        LocalDateTime currDateTime = LocalDateTime.now();
        OrderItem orderItem1 = createOrderItem(product1);
        OrderItem orderItem2 = createOrderItem(product2);

        Order order1 = createOrder(member, orderItem1, currDateTime, ORDERED);
        Order order2 = createOrder(member, orderItem2, currDateTime, COMPLETED);
        orderRepository.saveAll(List.of(order1, order2));

        // when
        List<Order> findOrders = orderRepository.findAllByOrderStatusIn(List.of(ORDERED));

        // then
        assertThat(findOrders).hasSize(1)
                .extracting("orderStatus", "orderDate")
                .containsExactlyInAnyOrder(
                        Tuple.tuple(ORDERED, currDateTime)
                );
    }

    private Order createOrder(Member member, OrderItem orderItem1, LocalDateTime orderDate, OrderStatus orderStatus) {
        return Order.builder()
                .orderStatus(orderStatus)
                .member(member)
                .delivery(Delivery.builder().build())
                .orderItems(List.of(orderItem1))
                .orderDate(orderDate)
                .build();
    }

    private OrderItem createOrderItem(Product product1) {
        return OrderItem.builder()
                .count(1)
                .orderPrice(5000)
                .product(product1)
                .build();
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