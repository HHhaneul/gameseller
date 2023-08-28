package org.shopping.controllers.buyer;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.shopping.CommonProcess;
import org.shopping.commons.MenuDetail;
import org.shopping.commons.libs.JavaScript;
import org.shopping.entities.Buyer;
import org.shopping.models.buyer.BuyerListService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller("BuyerController")
@RequestMapping("/admin/buyer")
@RequiredArgsConstructor
public class BuyerController implements CommonProcess {/*

    private final BuyerListService listService;

    private String tplCommon = "admin/buyer/";

    private final HttpServletRequest request;

    // 주문 목록 조회
    @GetMapping("/list")
    public String list(Model model) {

        commonProcess(model, "list");

        List<Buyer> buyersDone = listService.getBuyerDone();
        model.addAttribute("items", buyersDone);

        return tplCommon + "list";
    }

    // 주문 목록 조회
    @GetMapping
    public String index(Model model) {
        commonProcess(model, "list");
        System.out.println("buyer index");
        return tplCommon + "index";
    }

    // 주문 목록 상세조회
    @GetMapping("/view/{buyerNo}")
    public String view(@PathVariable Long buyerNo, Model model) {
        commonProcess(model, "view");
        BuyerForm buyerForm = listService.getBuyerForm(buyerNo);
        model.addAttribute("buyerForm", buyerForm);

        return tplCommon + "view";
    }

    @Override
    public void commonProcess(Model model, String mode) {

        String pageTitle = "주문 목록";
        if (mode.equals("view")) {
            pageTitle = "주문 상세";
        } else if (mode.equals("edit")) {
            pageTitle = "주문 수정";
        }

        CommonProcess.super.commonProcess(model, pageTitle);

        List<String> addCommonScript = new ArrayList<>();
        List<String> addScript = new ArrayList<>();
        if (mode.equals("add") || mode.equals("edit") || mode.equals("save")) {
            addCommonScript.add("ckeditor/ckeditor");
            addCommonScript.add("fileManager");
            addScript.add("buyer/form");
        }
        model.addAttribute("menuCode", "buyer");
        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);

        // 서브 메뉴 처리
        String subMenuCode = GameMenus.getSubMenuCode(request);
        model.addAttribute("subMenuCode", subMenuCode);

        // 서브 메뉴 조회
        List<MenuDetail> submenus = GameMenus.gets("buyer");
        model.addAttribute("submenus", submenus);
    }
*/
}
