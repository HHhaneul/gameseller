package org.shopping.models.support;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.shopping.commons.Utils;
import org.shopping.commons.validators.RequiredValidator;
import org.shopping.controllers.admins.support.RepeatQnAForm;
import org.shopping.entities.RepeatedQnA;
import org.shopping.repositories.RepeatedQnARepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RepeatQnASaveService implements RequiredValidator {

    private final RepeatedQnARepository repository;
    private final Utils utils;

    public void save(RepeatQnAForm form) {
        /* 필수 항목 검증 - question */
        requiredCheck(form.getQuestion(), utils.getMessage("NotBlank.question", "validation"));


        RepeatedQnA repeatedQnA = new ModelMapper().map(form, RepeatedQnA.class);

        repository.saveAndFlush(repeatedQnA);
    }


    /* 목록 저장 처리 */
    public void saveList(RepeatQnAForm form){
        List<Integer> chks = form.getChkNo();
        nullCheck(chks, utils.getMessage("NotSelected.edit", "validation"));

        for (Integer chk : chks) {
            String _id = utils.getParam("_id_" + chk);
            RepeatedQnA item = repository.findById(Long.valueOf(_id)).orElse(null);
            if (item == null) continue;

            item.setQuestion(utils.getParam("question_" + chk));
            item.setAnswer(utils.getParam("answer_" + chk));
            item.setUse(Boolean.parseBoolean(utils.getParam("use_" + chk)));
            item.setListQnA(Long.parseLong(utils.getParam("listQnA_" + chk)));
        }
        repository.flush();
    }
}

