package com.jpashop.api.service.product.request;

import com.jpashop.domain.product.Product;
import com.jpashop.domain.product.SellingStatus;
import lombok.Builder;

public class ProductCreateServiceRequest {

    private String name;

    private int price;

    private int stockQuantity;

    @Builder
    public ProductCreateServiceRequest(String name, int price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public Product toEntity() {
        return Product.builder()
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .sellingStatus(SellingStatus.HOLD)
                .build();
    }
}
