package org.shopping.repositories.order;

import org.shopping.entities.CartInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CartInfoRepository extends JpaRepository<CartInfo, Long>, QuerydslPredicateExecutor<CartInfo> {

}
