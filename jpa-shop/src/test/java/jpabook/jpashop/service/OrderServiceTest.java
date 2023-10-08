package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;
    
    @DisplayName("상품 주문")
    @Test
    void createOrder() {
        // given
        Member member = createMember("kang");
        memberRepository.save(member);

        int quantity = 10;
        Item book = createBook("JPA BOOK", 10000, quantity);
        itemRepository.save(book);

        // when
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        int currentStockQuantity = quantity - orderCount;

        // then
        Order findOrder = orderRepository.findById(orderId);
        assertThat(findOrder).isNotNull();
        assertThat(findOrder.getStatus()).isEqualByComparingTo(OrderStatus.ORDER);
        assertThat(findOrder.getOrderItems().size()).isEqualTo(1);
        assertThat(findOrder.getTotalPrice()).isEqualTo(10000 * orderCount);
        assertThat(book.getStockQuantity()).isEqualTo(currentStockQuantity);
    }

    @DisplayName("상품의 재고수량이 초과되면 예외 발생")
    @Test
    void overStockQuantity() {
        // given
        Member member = createMember("kang");
        memberRepository.save(member);

        int quantity = 10;
        Item book = createBook("JPA BOOK", 10000, quantity);
        itemRepository.save(book);

        // when & then
        int orderCount = 11;
        assertThatThrownBy(() -> orderService.order(member.getId(), book.getId(), orderCount))
                .isInstanceOf(NotEnoughStockException.class)
                .hasMessage("재고가 남아있지 않습니다.");
    }


    @DisplayName("주문 취소")
    @Test
    void cancelOrder() {
        // given
        Member member = createMember("kang");
        memberRepository.save(member);

        int quantity = 10;
        Item book = createBook("JPA BOOK", 10000, quantity);
        itemRepository.save(book);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // when
        orderService.cancelOrder(orderId);
    
        // then
        Order findOrder = orderRepository.findById(orderId);
        System.out.println("findOrder = " + findOrder);
        assertThat(findOrder.getStatus()).isEqualByComparingTo(OrderStatus.CANCEL);
        assertThat(book.getStockQuantity()).isEqualTo(quantity);
    }

    private Member createMember(String name) {
        Address address = new Address("01010", "서울시 구로구", "빌라 5층");

        return Member.builder()
                .name(name)
                .address(address)
                .build();
    }

    private Item createBook(String name, int price, int stockQuantity) {
        return Book.builder()
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .author("kang")
                .build();
    }
}