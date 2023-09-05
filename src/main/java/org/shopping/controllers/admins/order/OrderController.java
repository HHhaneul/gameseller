package org.shopping.controllers.admins.order;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.shopping.CommonProcess;
import org.shopping.commons.AlertException;
import org.shopping.commons.CommonException;
import org.shopping.commons.ListData;
import org.shopping.commons.ScriptExceptionProcess;
import org.shopping.commons.constants.OrderStatus;
import org.shopping.commons.menus.GameMenus;
import org.shopping.commons.menus.MenuDetail;
import org.shopping.controllers.orders.OrderForm;
import org.shopping.controllers.orders.OrderSearch;
import org.shopping.entities.OrderInfo;
import org.shopping.models.order.OrderInfoService;
import org.shopping.models.order.OrderSaveService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller("adminOrderController")
@RequestMapping("/admin/order")
@RequiredArgsConstructor
public class OrderController implements CommonProcess, ScriptExceptionProcess {

    private String tplCommon = "admin/order/";
    private final HttpServletRequest request;
    private final OrderInfoService orderInfoService;
    private final OrderSaveService orderSaveService;

    @GetMapping
    public String index(@ModelAttribute OrderSearch search, Model model) {
        commonProcess(model, "list");
        ListData<OrderInfo> data = orderInfoService.getList(search);

        model.addAttribute("items", data.getContent());
        model.addAttribute("pagination", data.getPagination());

        return tplCommon + "index";
    }

    @GetMapping("/{orderNo}")
    public String view(@PathVariable Long orderNo, Model model) {
        commonProcess(model, "view");
        OrderForm data = orderInfoService.getForm(orderNo);

        model.addAttribute("orderForm", data);


        return tplCommon + "view";
    }


    @PostMapping
    public String save(@Valid OrderForm orderForm, Errors errors, Model model) {
        commonProcess(model, "view");
        orderInfoService.updateInfo(orderForm);

        if (errors.hasErrors()) {
            return tplCommon + "view";
        }
        try {
            orderSaveService.update(orderForm);

            return "redirect:/admin/order"; // 주문서 수정 완료 후 주문서 목록으로 이동
        } catch (CommonException e) {
            e.printStackTrace();
            throw new AlertException(e.getMessage());
        }
    }

    public void commonProcess(Model model, String mode) {
        String pageTitle = "주문 목록";

        CommonProcess.super.commonProcess(model, pageTitle);

        List<String> addScript = new ArrayList<>();
        List<String> addCommonScript = new ArrayList<>();

        if (mode.equals("view")) { // 주문서 관리
            addCommonScript.add("address");
        }

        model.addAttribute("addScript", addScript);
        model.addAttribute("addCommonScript", addCommonScript);

        model.addAttribute("menuCode", "order");
        // 서브 메뉴 처리
        String subMenuCode = GameMenus.getSubMenuCode(request);
        if (mode.equals("view")) subMenuCode = "order";

        model.addAttribute("subMenuCode", subMenuCode);

        // 서브 메뉴 조회
        List<MenuDetail> submenus = GameMenus.gets("order");
        model.addAttribute("submenus", submenus);

        model.addAttribute("orderStatuses", OrderStatus.getList());
    }
}
