package org.shopping.repositories;

import com.querydsl.core.BooleanBuilder;
import org.shopping.entities.Category;
import org.shopping.entities.QCategory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

import static org.springframework.data.domain.Sort.Order.*;

public interface CategoryRepository extends JpaRepository<Category, String>, QuerydslPredicateExecutor<Category> {

    default List<Category> getList(String mode) {
        QCategory category = QCategory.category;
        BooleanBuilder builder = new BooleanBuilder();
        /** 전체 조회가 아니면 사용중인 분류만 조회 */
        if (!mode.equals("all")) builder.and(category.use.eq(true));
        List<Category> items = (List<Category>)findAll(builder, Sort.by(desc("listOrder"), asc("createdAt")));

        return items;
    }

    /**
     * 분류 전체목록 조회
     *
     */
    default List<Category> getListAll() {
        return getList("all");
    }

    /**
     * 사용중 분류 목록 조회
     *
     */
    default List<Category> getList() {
        return getList("use");
    }

    /**
     * 분류코드 중복 여부
     *
     */
    default boolean exists(String cateCd) {
        return exists(QCategory.category.cateCd.eq(cateCd));
    }
}
