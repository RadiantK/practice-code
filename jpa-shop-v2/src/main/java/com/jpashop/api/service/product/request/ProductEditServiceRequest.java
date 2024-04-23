package com.jpashop.api.service.product.request;

import com.jpashop.domain.product.SellingStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductEditServiceRequest {

    private Long id;

    private String name;

    private int price;

    private int stockQuantity;

    private SellingStatus sellingStatus;

    @Builder
    public ProductEditServiceRequest(Long id, String name, int price, int stockQuantity, SellingStatus sellingStatus) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.sellingStatus = sellingStatus;
    }
}
