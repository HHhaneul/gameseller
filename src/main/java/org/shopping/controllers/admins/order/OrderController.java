package org.shopping.controllers.admins.order;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.shopping.CommonProcess;
import org.shopping.commons.ListData;
import org.shopping.commons.menus.GameMenus;
import org.shopping.commons.menus.MenuDetail;
import org.shopping.controllers.orders.OrderSearch;
import org.shopping.entities.OrderInfo;
import org.shopping.models.order.OrderInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller("adminOrderController")
@RequestMapping("/admin/order")
@RequiredArgsConstructor
public class OrderController implements CommonProcess {

    private String tplCommon = "admin/order/";
    private final HttpServletRequest request;
    private final OrderInfoService orderInfoService;

    @GetMapping
    public String index(@ModelAttribute OrderSearch search, Model model) {
        commonProcess(model, "list");
        ListData<OrderInfo> data = orderInfoService.getList(search);
        data.getContent().stream().forEach(System.out::println);

        model.addAttribute("items", data.getContent());
        model.addAttribute("pagination", data.getPagination());

        return tplCommon + "index";
    }
    
    public void commonProcess(Model model, String mode) {
        String pageTitle = "주문 목록";

        CommonProcess.super.commonProcess(model, pageTitle);

        List<String> addScript = new ArrayList<>();
        List<String> addCommonScript = new ArrayList<>();



        model.addAttribute("addScript", addScript);
        model.addAttribute("addCommonScript", addCommonScript);

        model.addAttribute("menuCode", "order");
        // 서브 메뉴 처리
        String subMenuCode = GameMenus.getSubMenuCode(request);
        model.addAttribute("subMenuCode", subMenuCode);

        // 서브 메뉴 조회
        List<MenuDetail> submenus = GameMenus.gets("order");
        model.addAttribute("submenus", submenus);
    }
}
