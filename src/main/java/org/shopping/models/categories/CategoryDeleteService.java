package org.shopping.models.categories;

import lombok.RequiredArgsConstructor;
import org.shopping.commons.Utils;
import org.shopping.commons.validators.RequiredValidator;
import org.shopping.controllers.admins.game.CategoryForm;
import org.shopping.entities.Category;
import org.shopping.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryDeleteService implements RequiredValidator {
    private final CategoryRepository repository;
    private final Utils utils;

    /**
     * 분류 삭제
     *
     * @param cateCd
     */
    public void delete(String cateCd) {
        Category item = repository.findById(cateCd).orElse(null);
        if (item != null) {
            repository.delete(item);
            repository.flush();
        }

    }

    /**
     *  목록 삭제 처리
     *
     */
    public void deleteList(CategoryForm form) {
        List<Category> items = new ArrayList<>();
        List<Integer> chks = form.getChkNo();
        nullCheck(chks, utils.getMessage("NotSelected.delete", "validation"));

        for (Integer chk : chks) {
            String cateCd = utils.getParam("cateCd_" + chk);
            Category item = repository.findById(cateCd).orElse(null);
            if (item == null) continue;

            items.add(item);
        }
        repository.deleteAll(items);
        repository.flush();
    }
}