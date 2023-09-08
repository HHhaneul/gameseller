package org.shopping.controllers.members;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.shopping.CommonProcess;
import org.shopping.commons.menus.FrontMenus;
import org.shopping.commons.menus.MenuDetail;
import org.shopping.entities.RepeatedQnA;
import org.shopping.models.support.RepeatQnAInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/support")
@RequiredArgsConstructor
public class SupportController implements CommonProcess {

    private final RepeatQnAInfoService infoService;
    private final HttpServletRequest request;

    @GetMapping("/inquire")
    public String support(Model model) {

        commonProcess(model, "inquire");
        List<RepeatedQnA> items = infoService.getListAll();
        model.addAttribute("items", items);



        return "support/inquire";
    }

    @GetMapping("/repeatedly")
    public String repeated(Model model){
        commonProcess(model, "repeat");
        List<RepeatedQnA> items = infoService.getList("faq");
        model.addAttribute("items", items);


        return "support/repeatedly";
    }

    @Override
    public void commonProcess(Model model, String mode) {

        String pageTitle = "";
        if (mode == "repeat"){
            pageTitle = "자주 묻는 질문";
        } else if (mode == "inquire") {
            pageTitle = "질문하기";
            
        }

        List<String> addCommonScript = new ArrayList<>();
        List<String> addScript = new ArrayList<>();

        addScript.add("support");


        String menuCode = FrontMenus.getSubMenuCode(request);
        model.addAttribute("menuCode", "support");
        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);

        // 서브 메뉴 처리
        model.addAttribute("subMenuCode", menuCode);

        // 서브 메뉴 조회
        List<MenuDetail> submenus = FrontMenus.gets("support");
        model.addAttribute("submenus", submenus);

        CommonProcess.super.commonProcess(model, pageTitle);
    }
}
