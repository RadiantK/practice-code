package com.jpashop.api.service.product.request;

import com.jpashop.domain.product.SellingStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ProductServiceRequest {

    private List<SellingStatus> sellingStatuses = new ArrayList<>();

    @Builder
    public ProductServiceRequest(List<SellingStatus> sellingStatuses) {
        this.sellingStatuses = sellingStatuses;
    }
}
