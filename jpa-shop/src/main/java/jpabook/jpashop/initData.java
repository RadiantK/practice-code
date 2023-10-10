package jpabook.jpashop;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.time.LocalDateTime;

/**
 * 총 주문 2개
 * uesrA
 *  JPA1 BOOK
 *  JPA2 BOOK
 * userB
 *  SPRING1 BOOK
 *  SPRING2 BOOK
 */
@Profile("local")
@Component
@RequiredArgsConstructor
public class initData {

    private final InitService initService;

    /**
     *  트랜잭선 적용 등의 문제때문에 이런식으로 분리해서 로직을 사용하는 것이 좋다.
     */
    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void dbInit1() {
            Member member1 = createMember("userA", "서울", "강남", "11111");

            em.persist(member1);

            Book book1 = createBook("JPA1 BOOK", 10000, 100);

            Book book2 = createBook("JPA1 BOOK", 20000, 100);

            em.persist(book1);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery1 = setDelivery(member1.getAddress());

            Order order1 = Order.createOrder(member1, delivery1, LocalDateTime.now(), orderItem1, orderItem2);

            em.persist(order1);
        }

        public void dbInit2() {
            Member member2 = createMember("userB", "부산", "서면", "22222");

            em.persist(member2);

            Book book3 = createBook("SPRING1 BOOK", 30000, 200);

            Book book4 = createBook("SPRING2 BOOK", 40000, 300);

            em.persist(book3);
            em.persist(book4);

            OrderItem orderItem3 = OrderItem.createOrderItem(book3, 30000, 5);
            OrderItem orderItem4 = OrderItem.createOrderItem(book4, 40000, 10);

            Delivery delivery2 = setDelivery(member2.getAddress());

            Order order2 = Order.createOrder(member2, delivery2, LocalDateTime.now(), orderItem3, orderItem4);

            em.persist(order2);
        }

        private Delivery setDelivery(Address address) {
            return Delivery.builder()
                    .address(address)
                    .build();
        }

        private Book createBook(String name, int price, int stockQuantity) {
            return Book.builder()
                    .name(name)
                    .price(price)
                    .stockQuantity(stockQuantity)
                    .build();
        }

        private Member createMember(String userA, String city, String street, String zipcode) {
            return Member.builder()
                    .name(userA)
                    .address(new Address(city, street, zipcode))
                    .build();
        }
    }
}
