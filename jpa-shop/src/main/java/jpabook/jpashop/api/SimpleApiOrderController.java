package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.dto.OrderSearch;
import jpabook.jpashop.repository.query.OrderSimpleQueryDto;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.query.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * xToOne
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class SimpleApiOrderController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    /**
     *  * 엔티티를 직접 사용 시 문제
     *
     *  1. Order <-> Member 순환참조 문제로 무한 루프 -> 한쪽 객체에 @JsonIgnore 어노테이션 사용
     *  2. 지연 로딩 시 ByteBuddyInterceptor라는 객체로 프록시로 감싸게 되는데 json이 해석을 하지 못해 타입 에러 발생
     *  3. hibernate5Module 사용으로 해결 (잘 사용되지 않음)
     *
     *  그렇다고 FetchType.EAGER를 사용하면 성능 최적화가 되지 않음
     *  일반적인 findAll 함수는 jpql이 sql로 번역되어 전송되는데 처음에 Order 객체 데이터만 조회한 뒤 Eager 속성이면
     *  이후 연관된 데이터를 모두 가져오게되며 n + 1 문제가 발생됨
     *
     *  결과적으로 엔티티를 외부로 노출하게 되면 원하지 않는 정보 모두 노출되게 되며
     *  엔티티값이 변하면 api spec도 변하게 되는 문제가 발생하게 된다. -> 유지보수가 매우 힘듬
     */
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> orders = orderRepository.findAll(new OrderSearch());
        for (Order order : orders) {
            order.getMember().getName();
            order.getDelivery().getAddress();
        }

        return orders;
    }

    /**
     *  * dto를 사용했음에도 발생하는 성능 문제
     *
     *  dto로 변환하여 원하는 api 스펙을 응답하는 것은 성공
     *  하지만 lazy loading으로 인한 너무 많은 쿼리가 발생하는 문제 발생
     *  영속성 컨텍스트가 LAZY를 초기화해서 db에서 쿼리를 조회
     *
     *  N + 1 문제 발생
     *  Order -> 결과 두 개
     *  Loop를 돌면서 Member에 대해 Lazy Loading이 초기화 되면서 쿼리 두 번 발생
     *  그 후 Delivery에 대해 Lazy Loading이 초기화 되면서 쿼리 두 번 발생
     *
     *  만약 같은 회원인 경우 조회시 영속성 컨텍스트를 확인하기 때문에 최소는 1번이지만 최대는 회원 수만큼 쿼리가 발생한다.
     *  즉, 식별자(pk)가 같은 것을 조회하게 되면 영속 컨텍스트에서 관리되는 엔티티를 조회하기 때문에 쿼리가 하나만 발생한다.
     *
     *  엔티티를 Eager로 해도 쿼리에서 한번에 가져오지 않는 이유는 jpql은 조히하는 주체가 되는 엔티티만 조회해서 영속화하기 때문에
     *  Order를 조회할 때는 주체인 Order만 가져오고, 그 다음에 Eager인 객체를 확인 후 나머지 객체를 가져오 된다.
    */
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAll(new OrderSearch());

        return orders.stream()
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());
    }

    /**
     *  * 페치조인을 사용한 해결
     *
     *  페치 조인은 조회의 주체가 되는 엔티티 외에 Fetch Join이 걸린 연관 엔티티도 함께 SELECT 해서 모두 영속화하게 된다.
     *
     *  일반 Join은 join 조건을 제외하고 실제 질의하는 대상 Entity에 대한 컬럼만 SELECT
     *  Fetch Join은 실제 질의하는 대상 Entity와 Fetch join이 걸려있는 Entity를 포함한 컬럼 함께 SELECT
     *
     *  일반 Join을 사용하는 경우는 JPA는 DB와 객체 사이의 일관성을 고려해서 사용해야 하는데
     *  우리가 원하는 데이터는 Order의 데이터인데 Member의 name에 대한 조건절을 작성해야 할 때
     *  일반 Join을 사용하고 회원의 이름에 대한 조건절을 주게되면 쿼리는 Order를 가져오는 쿼리만 사용하며 Order 데이터를 가져올 수 있다.
     *  즉, 연관 관계가 있는 Entity가 쿼리 검색 조건에는 필요하지만 실제 데이터는 필요하지 않을 때 사용할 수 있다.
     *
     *  select o from Order o join o.member m where m.name = :name
     */
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();

        return orders.stream()
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());
    }

    /**
     *  * 쿼리에서 Dto를 리턴하도록 해서 원하는 데이터를 가져옴
     *
     *  v3와는 trade-off 관계
     *
     *  v4가 성능 최적화는 됐지만 단점도 존재한다.
     *  그리고 v4는 엔티티가 아닌 dto를 가져오기 때문에 데이터 가공 등의 행동을 했을 때 영속 컨텍스트에 반영되지 못한다.
     *  v4는 화면에 출력되는 API 스펙에 맞춰 데이터를 가져왔기 때문에 리포지토리 재사용성이 거의 없다.
     *  리포지토리는 일반적으로 db관련 로직을 작성하는데 api로직이 repository 계층에 들어가는 단점이 존재한다.
     *  그리고 요즘 네트워크 성능이 좋아져서 그렇게까지 많은 성능 차이가 나지는 않는다.
     *  성능은 필드보다는(필드가 엄청 많으면 다름) 조건절에 데이터가 인덱스를 타느냐가 더 중요하다.
     *
     *  v3는 엔티티를 모두 조회한 뒤 원하는 데이터를 가공해서 사용했기 때문에 재사용성이 높다.
     *
     *  리포지토리는 엔티티 그래프를 탐색하는 용도로 사용하는 것이 좋다.
     *  별도로 성능 최적화용 패키지를 분리해서 리포지토리를 구분한 뒤 쿼리를 생성하는 것이 좋다.
     *
     *  * 쿼리 방식 선택 권장 순서
     *  1. 우선 엔티티를 DTO로 변환하는 방법을 선택한다.
     *  2. 필요하면 페치 조인으로 성능을 최적화 한다. 대부분의 성능 이슈가 해결된다.
     *  3. 그래도 안되면 DTO로 직접 조회하는 방법을 사용한다.
     *  4. 최후의 방법은 JPA가 제공하는 네이티브 SQL이나 스프링 JDBC Template을 사용해서 SQL을 직접 사용한다.
     */
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderSimpleQueryRepository.findOrderDtos();
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getName();
            this.orderDate = order.getOrderDate();
            this.orderStatus = order.getStatus();
            this.address = order.getDelivery().getAddress();
        }
    }
}
