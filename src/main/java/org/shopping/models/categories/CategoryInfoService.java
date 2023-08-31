package org.shopping.models.categories;

import lombok.RequiredArgsConstructor;
import org.shopping.entities.Category;
import org.shopping.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 분류 조회
 *
 */
@Service
@RequiredArgsConstructor
public class CategoryInfoService {

    private final CategoryRepository repository;

    /**
     * 카테고리 조회
     * @param cateCd
     * @return
     */
    public Category get(String cateCd) {

        return repository.findById(cateCd).orElse(null);
    }

    /**
     * 사용중 분류 목록 조회
     *
     */
    public List<Category> getList() {
        return repository.getList();
    }


    /**
     * 분류 전체목록 조회
     *
     */
    public List<Category> getListAll() {
        return repository.getList("all");
    }
}