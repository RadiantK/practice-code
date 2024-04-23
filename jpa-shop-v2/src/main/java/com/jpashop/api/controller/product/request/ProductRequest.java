package com.jpashop.api.controller.product.request;

import com.jpashop.api.service.product.request.ProductServiceRequest;
import com.jpashop.domain.product.SellingStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ProductRequest {

    @NotEmpty(message = "상품 판매상태는 필수입니다.")
    private List<SellingStatus> sellingStatuses = new ArrayList<>();

    @Builder
    private ProductRequest(List<SellingStatus> sellingStatuses) {
        this.sellingStatuses = sellingStatuses;
    }

    public ProductServiceRequest toServiceRequest() {
        return new ProductServiceRequest(sellingStatuses);
    }
}
