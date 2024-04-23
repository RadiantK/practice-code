package com.jpashop.domain.order;

import com.jpashop.domain.BaseEntity;
import com.jpashop.domain.delivery.Delivery;
import com.jpashop.domain.delivery.DeliveryStatus;
import com.jpashop.domain.member.Member;
import com.jpashop.domain.orderItem.OrderItem;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_member_id"), nullable = false)
    private Member member;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime orderDate;

    public void changeStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Builder
    private Order(OrderStatus orderStatus, Member member, Delivery delivery, LocalDateTime orderDate) {
        this.orderStatus = orderStatus;
        this.member = member;
        this.delivery = delivery;
        this.orderDate = orderDate;
    }

    public static Order createOrder(Member member, Delivery delivery, LocalDateTime orderDate, OrderItem orderItem) {
        Order order = new Order(OrderStatus.ORDERED, member, delivery, orderDate);
        // 예제이므로 주문 상품은 하나만 add
        order.getOrderItems().add(orderItem);

        return order;
    }

    public void cancel() {
        if (DeliveryStatus.COMPLETE.equals(delivery.getStatus())) {
            throw new IllegalArgumentException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        changeStatus(OrderStatus.CANCELED);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }
}
