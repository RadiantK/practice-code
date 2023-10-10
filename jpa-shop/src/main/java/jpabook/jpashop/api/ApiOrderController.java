package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.dto.OrderSearch;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.query.OrderFlatDto;
import jpabook.jpashop.repository.query.OrderItemQueryDto;
import jpabook.jpashop.repository.query.OrderQueryDto;
import jpabook.jpashop.repository.query.OrderQueryRepository;
import jpabook.jpashop.service.query.OrderQueryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

/**
 * xToMany 컬렉션 조회 성능 최적화
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class ApiOrderController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    // OPEN_SESSION_IN_VIEW용 CQRS
    private final OrderQueryService orderQueryService;

    /**
     *  * 엔티티 직접 노출 문제
     *
     *  프록시를 강제 초기화 한 뒤 데이터를 hibernate5Module 사용으로 출력
     *
     *  엔티티를 직접 노출하기 때문에 양방향 연관관계에서 @JsonIgnore를 사용해서
     *  노출하지 않는 쪽의 엔티티를 출력하지 않도록 해야 함.
     */
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {

        // open_session_in_view 속성을 false로 사용하기 위해 query(조회) 로직을 별도의 서비스로 분리
        return orderQueryService.findOrders();
    }

    /**
     *  * dto로 변환 후 리턴
     *
     *  dto로 반환하는 것은 엔티티를 외부에 노출시켜선 안되며 List등에 엔티티를 wrapping하는 것도 안된다.
     */
    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAll(new OrderSearch());

        return orders.stream()
                .map(OrderDto::new)
                .collect(toList());
    }

    /**
     *  * fetch 조인을 사용한 n + 1 문제 해결
     *
     *  orderRepository.findAllWithItem() 주석 확인
     *
     *  컬렉션을 페치 조인하면 페이징이 불가능한 문제가 발생한다.
     *  컬렉션을 페치 조인하면 일대다 조인이 발생하므로 데이터가 예측할 수 없이 증가한다.
     *  일다대에서 일(1)을 기준으로 페이징을 하는 것이 목적이다. 그런데 데이터는 다(N)를 기준으로 row 가 생성된다.
     *  Order를 기준으로 페이징 하고 싶은데, 다(N)인 OrderItem을 조인하면 OrderItem이 기준이 되어버린다.
     *
     *  컬렉션 페치 조인은 하나의 컬렉션에 대해서만 사용할 수 있다.
     *  컬렉션안의 컬렉션이 있게되면 데이터가 뻥튀기 됨.
     */
    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();

        for (Order order : orders) {
            log.info("order ref={}, id={}", order, order.getId());
        }

        return orders.stream()
                .map(OrderDto::new)
                .collect(toList());
    }

    /**
     *  * 컬렉션 페치 조인 시 페이징 할 수 없는 문제 해결 (엔티티를 조회할 때만 사용 가능)
     *
     *  먼저 ToOne(OneToOne, ManyToOne) 관계를 모두 페치조인 한다. ToOne 관계는 row수를 증가시키지 않으므로 페이징 쿼리에 영향을 주지 않는다.
     *  컬렉션은 지연 로딩으로 조회한다.
     *  지연 로딩 성능 최적화를 위해 hibernate.default_batch_fetch_size , @BatchSize 를 적용한다.
     *  hibernate.default_batch_fetch_size: 글로벌 설정
     *  @BatchSize: 개별 최적화
     *  이 옵션을 사용하면 컬렉션이나, 프록시 객체를 한번에 설정한 size 만큼 IN 쿼리로 조회한다. (최적화)
     */
    @GetMapping("/api/v3_1/orders")
    public List<OrderDto> ordersV3_1(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit
    ) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);

        return orders.stream()
                .map(OrderDto::new)
                .collect(toList());
    }

    /**
     *  * dto로 조회하는 방식
     *
     *  Query: 루트 1번, 컬렉션 N 번 실행
     *  ToOne(N:1, 1:1) 관계들을 먼저 조회하고, ToMany(1:N) 관계는 각각 별도로 처리한다.
     *  이런 방식을 선택한 이유는 다음과 같다.
     *  ToOne 관계는 조인해도 데이터 row 수가 증가하지 않는다. ToMany(1:N) 관계는 조인하면 row 수가 증가한다.
     *  row 수가 증가하지 않는 ToOne 관계는 조인으로 최적화 하기 쉬우므로 한번에 조회하고,
     *  ToMany 관계는 최적화 하기 어려우므로 findOrderItems() 같은 별도의 메서드로 조회한다.
     */
    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4() {
        return orderQueryRepository.findOrderQueryDto();
    }

    /**
     *  * dto 조회 - 컬렉션 조회 최적화
     *
     *  Query: 루트 1번, 컬렉션 1번
     *  ToOne 관계들을 먼저 조회하고, 여기서 얻은 식별자 orderId로 ToMany 관계인 OrderItem 을 한꺼번에 조회
     *  MAP을 사용해서 매칭 성능 향상(O(1))
     */
    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5() {
        return orderQueryRepository.findAllByDto_optimization();
    }

    /**
     *  * dto 조회 -  플랫 데이터 최적화
     *
     *  query가 한 번 나감
     *
     *  쿼리는 한번이지만 조인으로 인해 DB에서 애플리케이션에 전달하는 데이터에 중복 데이터가 추가되므로 상황에 따라 V5 보다 더 느릴 수 도 있다.
     *  애플리케이션에서 추가 작업이 크다.
     *  페이징이 불가능 하다.
     */
    @GetMapping("/api/v6/orders")
    public List<OrderQueryDto> ordersV6() {
        List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();

        return flats.stream()
                .collect(groupingBy(o -> new OrderQueryDto(o.getOrderId(), o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
                        mapping(o -> new OrderItemQueryDto(o.getOrderId(), o.getItemName(), o.getOrderPrice(), o.getCount()), toList())
                ))
                .entrySet().stream()
                .map(e -> new OrderQueryDto(e.getKey().getOrderId(), e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(), e.getKey().getAddress(), e.getValue()))
                .collect(toList());
    }

    @Getter
    static class OrderDto {

        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
//        private List<OrderItem> orderItems;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getName();
            this.orderDate = order.getOrderDate();
            this.orderStatus = order.getStatus();
            this.address = order.getDelivery().getAddress();
            // 엔티티라서 조회 안되는 문제때문에 프록시를 강제 초기화해서 조회
//            order.getOrderItems().stream().forEach(orderItem -> orderItem.getItem().getName());
//            this.orderItems = order.getOrderItems();
            this.orderItems = order.getOrderItems().stream()
                    .map(OrderItemDto::new)
                    .collect(toList());
        }
    }

    @Getter
    static class OrderItemDto {

        private String itemName; // 상품명
        private int orderPrice; // 주문 가격
        private int count; // 주문 수량

        public OrderItemDto(OrderItem orderItem) {
            this.itemName = orderItem.getItem().getName();
            this.orderPrice = orderItem.getOrderPrice();
            this.count = orderItem.getCount();
        }
    }
}
