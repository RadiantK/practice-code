package com.jpashop.api.controller.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpashop.api.controller.order.request.OrderCancelRequest;
import com.jpashop.api.controller.order.request.OrderCreateRequest;
import com.jpashop.api.service.order.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @DisplayName("새로운 주문을 생성한다.")
    @Test
    void newOrder() throws Exception {
        // given
        OrderCreateRequest request = OrderCreateRequest.builder()
                .memberId(1L)
                .productId(1L)
                .count(5)
                .build();

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/orders/new")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("새로운 주문을 생성할 때 회원 아이디는 필수값이다.")
    @Test
    void newOrderWithoutMemberId() throws Exception {
        // given
        OrderCreateRequest request = OrderCreateRequest.builder()
                .memberId(null)
                .productId(1L)
                .count(5)
                .build();

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/orders/new")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath(".code").value(400))
        .andExpect(MockMvcResultMatchers.jsonPath(".message").value("회원 아이디는 필수입니다."))
        .andExpect(MockMvcResultMatchers.jsonPath(".httpStatus").value(HttpStatus.BAD_REQUEST.name()));
    }

    @DisplayName("새로운 주문을 생성할 때 주문 아이디는 필수값이다.")
    @Test
    void newOrderWithoutProductId() throws Exception {
        // given
        OrderCreateRequest request = OrderCreateRequest.builder()
                .memberId(1L)
                .productId(null)
                .count(5)
                .build();

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/orders/new")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath(".code").value(400))
        .andExpect(MockMvcResultMatchers.jsonPath(".message").value("상품 아이디는 필수입니다."))
        .andExpect(MockMvcResultMatchers.jsonPath(".httpStatus").value(HttpStatus.BAD_REQUEST.name()));
    }

    @DisplayName("새로운 주문을 생성할 떄 상품 수량은 양수이다.")
    @Test
    void newOrderIsCountPositive() throws Exception {
        // given
        OrderCreateRequest request = OrderCreateRequest.builder()
                .memberId(1L)
                .productId(1L)
                .count(0)
                .build();

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/orders/new")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath(".code").value(400))
        .andExpect(MockMvcResultMatchers.jsonPath(".message").value("상품수량은 양수만 가능합니다."))
        .andExpect(MockMvcResultMatchers.jsonPath(".httpStatus").value(HttpStatus.BAD_REQUEST.name()));
    }

    @DisplayName("주문을 취소한다.")
    @Test
    void cancelOrder() throws Exception {
        // given
        OrderCancelRequest request = OrderCancelRequest.builder()
                .orderId(1L)
                .build();

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/orders/cancel")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("주문을 취소할 때 주문 번호 필수이다.")
    @Test
    void cancelOrderWithoutOrderId() throws Exception {
        // given
        OrderCancelRequest request = OrderCancelRequest.builder()
                .orderId(null)
                .build();

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/orders/cancel")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath(".code").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath(".message").value("주문 번호는 필수입니다."))
                .andExpect(MockMvcResultMatchers.jsonPath(".httpStatus").value(HttpStatus.BAD_REQUEST.name()));
    }
}