package org.shopping.repositories.member;

import org.shopping.entities.MemberBoardData;
import org.shopping.entities.QMemberBoardData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface BoardDataRepository extends JpaRepository<MemberBoardData, Long>, QuerydslPredicateExecutor<MemberBoardData> {
    default boolean exists(Long id) {
        QMemberBoardData memberBoardData = QMemberBoardData.memberBoardData;
        return exists(memberBoardData.id.eq(id));
    }

}
