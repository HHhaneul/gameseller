package org.shopping.controllers.orders;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.shopping.CommonProcess;
import org.shopping.commons.exception.AlertException;
import org.shopping.commons.exception.CommonException;
import org.shopping.commons.ScriptExceptionProcess;
import org.shopping.commons.menus.FrontMenus;
import org.shopping.commons.menus.MenuDetail;
import org.shopping.entities.CartInfo;
import org.shopping.models.order.CartInfoSaveService;
import org.shopping.models.order.CartInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/order/cart")
@RequiredArgsConstructor
public class CartController implements CommonProcess, ScriptExceptionProcess {

    private final CartInfoSaveService saveService;
    private final CartInfoService infoService;
    private final HttpServletRequest request;


    @GetMapping
    public String index(@ModelAttribute CartForm form, Model model) {
        commonProcess(model, "cart");

        List<CartInfo> items = infoService.getList("cart");
        model.addAttribute("items", items);

        items.stream().forEach(System.out::println);

        return "cart/index";
    }


    @PostMapping
    public String cartPs(CartForm form, Model model) {

        try {
            saveService.save(form);

            String mode = form.getMode();
            String url = mode.equals("direct") ? "/order" : "/order/cart";

            String script = String.format("parent.location.replace('%s');", url);
            model.addAttribute("script", script);

            return "commons/_execute_script";
        } catch (CommonException e) {
            e.printStackTrace();
            throw new AlertException(e.getMessage());
        }
    }


    public void commonProcess(Model model, String mode) {
        String pageTitle = "장바구니";
        CommonProcess.super.commonProcess(model, pageTitle);
        List<String> addScript = new ArrayList<>();
        if (mode.equals("cart")) {
            addScript.add("order/cart");
        }
        List<String> addCss = new ArrayList<>();
        List<String> addCommonScript = new ArrayList<>();

        model.addAttribute("addScript", addScript);

        String menuCode = FrontMenus.getSubMenuCode(request);

        model.addAttribute("menuCode", "cart");
        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);

        // 서브 메뉴 처리
        model.addAttribute("subMenuCode", menuCode);

        // 서브 메뉴 조회
        List<MenuDetail> submenus = FrontMenus.gets(menuCode);
        model.addAttribute("submenus", submenus);

        CommonProcess.super.commonProcess(model, pageTitle);
    }
}
