package org.shopping.repositories;

import com.querydsl.core.BooleanBuilder;
import org.shopping.entities.QRepeatedQnA;
import org.shopping.entities.RepeatedQnA;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

import static org.springframework.data.domain.Sort.Order.asc;
import static org.springframework.data.domain.Sort.Order.desc;

public interface RepeatedQnARepository extends JpaRepository<RepeatedQnA, Long>, QuerydslPredicateExecutor<RepeatedQnA> {

    default List<RepeatedQnA> getList(String mode) {
        QRepeatedQnA repeatedQnA = QRepeatedQnA.repeatedQnA;
        BooleanBuilder builder = new BooleanBuilder();
        /** 전체 조회가 아니면 사용중인 분류만 조회 */
        if (!mode.equals("all")) builder.and(repeatedQnA.use.eq(true));
        List<RepeatedQnA> items = (List<RepeatedQnA>)findAll(builder, Sort.by(desc("listQnA"), asc("createdAt")));

        return items;
    }

    /**
     * 전체 목록 조회
     *
     */
    default List<RepeatedQnA> getListAll() {
        return getList("all");
    }

    /**
     * 자주 묻는 질문 조회
     *
     */
    default List<RepeatedQnA> getList() {
        return getList("use");
    }

    /**
     * 중복 여부
     *
     */
    default boolean exists(String question) {
        return exists(QRepeatedQnA.repeatedQnA.question.eq(question));
    }
}
