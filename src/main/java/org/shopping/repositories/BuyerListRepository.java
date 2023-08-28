package org.shopping.repositories;

import com.querydsl.core.BooleanBuilder;
import org.shopping.entities.Buyer;
import org.shopping.entities.QBuyer;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

import static org.springframework.data.domain.Sort.Order.asc;

public interface BuyerListRepository extends JpaRepository<Buyer, Long>, QuerydslPredicateExecutor<Buyer> { // mode : all - 모두, done - 주문완료, undone : 장바구니 상태
    default List<Buyer> getBuyers(String gid, String mode) {
        QBuyer buyer = QBuyer.buyer;

        BooleanBuilder builder = new BooleanBuilder();
        if (gid != null && !gid.isBlank()) builder.and(buyer.gid.eq(gid));
        if (mode != null && !mode.isBlank()) {
            if (mode.equals("done")) builder.and(buyer.orderDone.eq(true)); // 주문완료
            else if (mode.equals("undone")) builder.and(buyer.orderDone.eq(false)); // 장바구니 상태인 주문
        }
        List<Buyer> items = (List<Buyer>)findAll(builder, Sort.by(asc("buyerNm")));

        return items;
    }


}