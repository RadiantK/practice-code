package com.jpashop.domain.orderItem;

import com.jpashop.domain.order.Order;
import com.jpashop.domain.product.Product;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    private int orderPrice;

    private int count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_product_id"))
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_orders_id"))
    private Order order;

    public void setOrder(Order order) {
        this.order = order;
    }

    @Builder
    private OrderItem(int orderPrice, int count, Product product) {
        this.orderPrice = orderPrice;
        this.count = count;
        this.product = product;
    }
}
