package org.shopping.repositories;

import org.shopping.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface MemberListRepository extends JpaRepository<Member, Long>, QuerydslPredicateExecutor<Member> {
    Member findbyuserNm(Long userNm);
}
