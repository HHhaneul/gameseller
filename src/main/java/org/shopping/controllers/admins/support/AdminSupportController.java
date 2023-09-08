package org.shopping.controllers.admins.support;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shopping.CommonProcess;
import org.shopping.commons.exception.AlertException;
import org.shopping.commons.exception.CommonException;
import org.shopping.commons.menus.GameMenus;
import org.shopping.commons.menus.MenuDetail;
import org.shopping.entities.RepeatedQnA;
import org.shopping.models.support.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller("adminSupportController")
@RequestMapping("/admin/support")
@RequiredArgsConstructor
public class AdminSupportController implements CommonProcess {

    private final RepeatQnASaveService repeatQnASaveService;
    private final RepeatQnADeleteService repeatQnADeleteService;
    private final RepeatQnAInfoService repeatQnAInfoService;

    private final HttpServletRequest request;

    @GetMapping
    public String repeatAdd(Model model){

        commonProcess(model, "자주 묻는 질문");
        List<RepeatedQnA> items = repeatQnAInfoService.getListAll();
        log.info("갯수 : " + items.size());
        model.addAttribute("items", items);

        return "admin/support/index";
    }

    @PostMapping
    public String repeatSave(RepeatQnAForm form, Model model){
        commonProcess(model, "자주 묻는 질문");

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

        List<String> addCommonScript = new ArrayList<>();
        List<String> addScript = new ArrayList<>();


        model.addAttribute("menuCode", "support");
        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);

        // 서브 메뉴 처리
        String subMenuCode = GameMenus.getSubMenuCode(request);
        model.addAttribute("subMenuCode", subMenuCode);

        // 서브 메뉴 조회
        List<MenuDetail> submenus = GameMenus.gets("support");
        model.addAttribute("submenus", submenus);

        CommonProcess.super.commonProcess(model, pageTitle);
    }
}
