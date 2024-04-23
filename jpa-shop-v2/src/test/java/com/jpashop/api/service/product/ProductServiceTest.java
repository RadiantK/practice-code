package com.jpashop.api.service.product;

import com.jpashop.api.controller.product.request.ProductCreateRequest;
import com.jpashop.api.controller.product.request.ProductRequest;
import com.jpashop.api.service.product.response.ProductCreateResponse;
import com.jpashop.api.service.product.response.ProductResponse;
import com.jpashop.domain.product.Product;
import com.jpashop.domain.product.ProductRepository;
import com.jpashop.domain.product.SellingStatus;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.jpashop.domain.product.SellingStatus.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private EntityManager em;

    @DisplayName("판매중인 모든 상품 목록을 조회한다.")
    @Test
    void findProducts() {
        // given
        Product product1 = createProduct("노트", 5000, 5, SELLING);
        Product product2 = createProduct("볼펜", 1000, 3, HOLD);
        Product product3 = createProduct("형광펜", 2000, 4, SELLING);
        Product product4 = createProduct("JPA책", 20000, 10, SELLING);
        productRepository.saveAll(List.of(product1, product2, product3, product4));


        ProductRequest request = createProductRequest(SELLING);

        // when
        List<ProductResponse> products = productService.findProducts(request.toServiceRequest());

        // then
        assertThat(products).hasSize(3)
                .extracting("name", "price", "stockQuantity", "sellingStatus")
                .containsExactlyInAnyOrder(
                        Tuple.tuple("노트", 5000, 5, SELLING),
                        Tuple.tuple("형광펜", 2000, 4, SELLING),
                        Tuple.tuple("JPA책", 20000, 10, SELLING)
                );
    }

    @DisplayName("판매중이 아닌 모든 상품 목록을 조회한다.")
    @Test
    void findProductsNotSelling() {
        // given
        Product product1 = createProduct("노트", 5000, 5, SELLING);
        Product product2 = createProduct("볼펜", 1000, 3, HOLD);
        Product product3 = createProduct("형광펜", 2000, 4, SELLING);
        Product product4 = createProduct("JPA책", 20000, 10, SELLING);
        productRepository.saveAll(List.of(product1, product2, product3, product4));


        List<SellingStatus> sellingStatus = List.of(HOLD, STOP_SELLING);
        ProductRequest request = createProductRequest(sellingStatus);

        // when
        List<ProductResponse> products = productService.findProducts(request.toServiceRequest());

        // then
        assertThat(products).hasSize(1)
                .extracting("name", "price", "stockQuantity", "sellingStatus")
                .contains(
                        Tuple.tuple("볼펜", 1000, 3, HOLD)
                );

        List<Product> results = productRepository.findAllBySellingStatusIn(sellingStatus);
        assertThat(results).hasSize(1)
                .extracting("name", "price", "stockQuantity", "sellingStatus")
                .contains(
                        Tuple.tuple("볼펜", 1000, 3, HOLD)
                );
    }

    @DisplayName("새로운 상품을 등록한다")
    @Test
    void saveProduct() {
        // given
        String name = "JPA책";
        int price = 1000;
        int stockQuantity = 100;
        ProductCreateRequest request = createProductCreateRequest(name, price, stockQuantity);

        // when
        ProductCreateResponse response = productService.saveProduct(request.toServiceRequest());

        // then
        assertThat(response.getName()).isEqualTo(name);
        assertThat(response.getPrice()).isEqualTo(price);
        assertThat(response.getStockQuantity()).isEqualTo(stockQuantity);

        Product savedProduct = productRepository.findById(response.getId()).orElse(null);
        assertThat(savedProduct.getName()).isEqualTo(name);
        assertThat(savedProduct.getPrice()).isEqualTo(price);
        assertThat(savedProduct.getStockQuantity()).isEqualTo(stockQuantity);
    }

    @DisplayName("기존 상품을 수정한다.")
    @Test
    void editProduct() {
        // given
        Product product = createProduct("노트", 5000, 5, HOLD);
        Product savedProduct = productRepository.save(product);

        // when
        String editName = "JPA책";
        int editPrice = 2000;
        int editQuantity = 100;
        SellingStatus editStatus = SELLING;
        savedProduct.editProductInfo(editName, editPrice, editQuantity, editStatus);
        em.flush();
        em.clear();

        // then
        Product findProduct = productRepository.findById(savedProduct.getId()).get();
        assertThat(findProduct.getName()).isEqualTo(editName);
        assertThat(findProduct.getPrice()).isEqualTo(editPrice);
        assertThat(findProduct.getStockQuantity()).isEqualTo(editQuantity);
        assertThat(findProduct.getSellingStatus()).isEqualTo(editStatus);
    }

    private ProductCreateRequest createProductCreateRequest(String name, int price, int stockQuantity) {
        return ProductCreateRequest.builder()
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .build();
    }

    private ProductRequest createProductRequest(SellingStatus sellingStatus) {
        return ProductRequest.builder()
                .sellingStatuses(List.of(sellingStatus))
                .build();
    }

    private ProductRequest createProductRequest(List<SellingStatus> sellingStatus) {
        return ProductRequest.builder()
                .sellingStatuses(sellingStatus)
                .build();
    }

    private Product createProduct(String name, int price, int stockQuantity, SellingStatus sellingStatus) {
        return Product.builder()
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .sellingStatus(sellingStatus)
                .build();
    }

}