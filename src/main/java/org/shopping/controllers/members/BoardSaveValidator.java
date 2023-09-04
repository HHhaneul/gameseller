package org.shopping.controllers.members;

import lombok.RequiredArgsConstructor;
import org.shopping.commons.MemberUtil;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class BoardSaveValidator implements Validator {
    private final MemberUtil memberUtil;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(MemberBoardForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MemberBoardForm form = (MemberBoardForm)target;
        if (!memberUtil.isLogin()) {
            String poster = form.getPoster();
            if (poster == null || poster.isBlank()) {
                errors.rejectValue("poster", "NotBlank");
            }
        }
    }
}
