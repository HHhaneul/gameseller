package org.shopping.controllers.admins.support;

import lombok.RequiredArgsConstructor;
import org.shopping.CommonProcess;
import org.shopping.commons.*;
import org.shopping.entities.RepeatedQnA;
import org.shopping.models.support.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller("adminSupportController")
@RequestMapping("/admin/support")
@RequiredArgsConstructor
public class AdminSupportController implements CommonProcess {

    private final RepeatQnASaveService repeatQnASaveService;
    private final RepeatQnADeleteService repeatQnADeleteService;
    private final RepeatQnAInfoService repeatQnAInfoService;

    @GetMapping
    public String repeatAdd(Model model){

        commonProcess(model, "자주 묻는 질문");
        List<RepeatedQnA> items = repeatQnAInfoService.getListAll();
        System.out.println("뭐야 왜 안뜨냐고" + items);
        model.addAttribute("items", items);

        return "admin/support/index";
    }

    @PostMapping
    public String repeatSave(RepeatQnAForm form, Model model){
        commonProcess(model, "");

        String mode = form.getMode();
        mode = mode == null || mode.isBlank() ? "add" : mode;
        try {
            if (mode.equals("add")) {

                repeatQnASaveService.save(form);
                /* 수정 */
            } else if (mode.equals("edit")) {

                repeatQnASaveService.saveList(form);
                /* 삭제 */
            } else if (mode.equals("delete")) {

                repeatQnADeleteService.deleteList(form);
            }
            /* 등록 */
        } catch (CommonException e) {
            e.printStackTrace();
            throw new AlertException(e.getMessage()); // 자바스크립트 alert 형태로 에러 출력
        }
        String script = "parent.location.reload();";
        model.addAttribute("script", script);
        return "commons/_execute_script";
    }

    @Override
    public void commonProcess(Model model, String pageTitle) {

        pageTitle = "QnA";


        CommonProcess.super.commonProcess(model, pageTitle);


    }
}
