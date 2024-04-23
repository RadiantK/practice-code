package com.jpashop.domain.product;

import com.jpashop.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    @Enumerated(value = EnumType.STRING)
    private SellingStatus sellingStatus;

    @Builder
    private Product(String name, int price, int stockQuantity, SellingStatus sellingStatus) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.sellingStatus = sellingStatus;
    }

    public boolean isEnoughStockQuantity(int quantity) {
        return stockQuantity >= quantity;
    }

    public void deductQuantity(int quantity) {
        if (!isEnoughStockQuantity(quantity)) {
            throw new IllegalArgumentException("재고 수량이 부족합니다.");
        }

        this.stockQuantity -= quantity;
    }

    public void editProductInfo(String name, int price, int stockQuantity, SellingStatus sellingStatus) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.sellingStatus = sellingStatus;
    }
}
