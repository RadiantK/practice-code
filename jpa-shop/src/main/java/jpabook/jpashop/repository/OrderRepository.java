package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.dto.OrderSearch;
import jpabook.jpashop.repository.query.OrderSimpleQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findById(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAll(OrderSearch orderSearch) {
        String jpql = "select o from Order o join o.member m";
        boolean isFirstCondition = true;

        // 주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }

        // 회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class) .setMaxResults(1000); //최대 1000건

        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }

        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }

        return query.getResultList();
    }

    /**
     *  한번의 쿼리로 order, member, delivery를 모두 가져옴
     */
    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
                "select o from Order o" +
                " join fetch o.member m" +
                " join fetch o.delivery d", Order.class)
                .getResultList();
    }

    /**
     *   Order와 OrderItems를 조인하면서 데이터가 두배가 되버림
     *   order 2개 orderitems 4개 -> 주문당 주문 상품 2개 -> 레코드가 4개가 나타남
     *   데이터 베이스의 조인 시 결과처럼 값이 나타남
     *
     *   이 문제 해결을 위해 distinct 사용
     *   db에서 distinct는 주문 상품의 상품 명이 다르기때문에 결과가 같게 나오지만
     *   jpa에서 distinct는 엔티티가 같으면 중복을 제거해준다.(같은 pk의 데이터를 하나 버림)
     *
     *   하지만 페이징이 불가능 하다는 문제가 있다.
     *   limit, offset 쿼리가 날라가지 않음
     *   warning log 발생: HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!
     *   메모리에서 페이징 처리를 한다는 로그 발생 쿼리의 결과를 애플리케이션에 로딩 후 페이징 처리를 함 (오버헤드 발생)
     *
     *   우리가 원하는 order는 두 개지만 실제 sql 결과 튜플은 4개가 되므로 우리가 원한 결과에 대해 페이징을 할 수 없기 때문에
     *   order에 대해서 페이징을 하기 위해서 데이터를 가져온 뒤 인 메모리에서 페이징 작업을 하게 된다.
     */
    public List<Order> findAllWithItem() {
        return em.createQuery(
                "select distinct o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d" +
                        " join fetch o.orderItems oi" +
                        " join fetch oi.item i", Order.class)
//                .setFirstResult(1)
//                .setMaxResults(100)
                .getResultList();
    }

    /**
     *  offset, limit 을 받는 페이징 처리
     *
     *  default_batch_fetch_size로 hibernate에서 최적화 해줌
     */
    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery(
                "select o from Order o" +
                        " join fetch o.member m" +
                        " join fetch o.delivery d", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
}
