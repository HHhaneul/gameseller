package org.shopping.models.support;

import lombok.RequiredArgsConstructor;
import org.shopping.commons.Utils;
import org.shopping.commons.validators.RequiredValidator;
import org.shopping.controllers.admins.support.RepeatQnAForm;
import org.shopping.entities.RepeatedQnA;
import org.shopping.repositories.RepeatedQnARepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RepeatQnADeleteService implements RequiredValidator {

    private final RepeatedQnARepository repository;
    private final Utils utils;
    public void delete(Long _id) {

        RepeatedQnA item = repository.findById(_id).orElse(null);
        if (item != null) {
            repository.delete(item);
            repository.flush();
        }
    }

    /**
     *  목록 삭제 처리
     *
     */
    public void deleteList(RepeatQnAForm form) {
        List<RepeatedQnA> items = new ArrayList<>();
        List<Integer> chks = form.getChkNo();
        nullCheck(chks, utils.getMessage("NotSelected.delete", "validation"));

        for (Integer chk : chks) {
            Long _id = Long.valueOf(utils.getParam("_id_" + chk));
            RepeatedQnA item = repository.findById(_id).orElse(null);
            if (item == null) continue;

            items.add(item);
        }
        repository.deleteAll(items);
        repository.flush();
    }
}
