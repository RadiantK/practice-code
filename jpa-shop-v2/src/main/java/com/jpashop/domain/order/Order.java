package com.jpashop.domain.order;

import com.jpashop.domain.BaseEntity;
import com.jpashop.domain.delivery.Delivery;
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

    @Builder
    private Order(OrderStatus orderStatus, Member member, Delivery delivery, List<OrderItem> orderItems, LocalDateTime orderDate) {
        this.orderStatus = orderStatus;
        this.member = member;
        this.delivery = delivery;
        this.orderItems = orderItems;
        this.orderDate = orderDate;
    }
}
