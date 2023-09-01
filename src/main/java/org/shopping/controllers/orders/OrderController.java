package org.shopping.controllers.orders;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shopping.CommonProcess;
import org.shopping.commons.AlertBackException;
import org.shopping.commons.CommonException;
import org.shopping.commons.ScriptExceptionProcess;
import org.shopping.entities.CartInfo;
import org.shopping.models.order.CartInfoService;
import org.shopping.models.order.CartItemNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController implements CommonProcess, ScriptExceptionProcess {
    private final CartInfoService cartInfoService;

    @GetMapping
    public String index(@ModelAttribute OrderForm form, Model model) {
        try {
            commonProcess(model, "form", form);

            return "order/index";
        } catch (CommonException e) {
            throw new AlertBackException(e.getMessage());
        }
    }

    @PostMapping
    public String indexPs(@Valid OrderForm form, Errors errors, Model model) {
        commonProcess(model, "form", form);

        if (errors.hasErrors()) {
            return "order/index";
        }

        return "commons/_execute_script";
    }

    public void commonProcess(Model model, String mode) {
        commonProcess(model, mode, null);
    }

    public void commonProcess(Model model, String mode, OrderForm form) {
        String pageTitle = "주문서 작성";
        CommonProcess.super.commonProcess(model, pageTitle);

        List<String> addScript = new ArrayList<>();
        List<String> addCommonScript = new ArrayList<>();
        if (mode.equals("form")) {
            addCommonScript.add("address");
            addScript.add("order/order");

            /** 주문서 양식인 경우 장바구니 상품 조회 S */
            List<Long> cartNo = form.getCartNo();
            List<CartInfo> items = null;
            if (cartNo == null || cartNo.isEmpty()) { // 바로 구매
                items = cartInfoService.getList("direct");
            } else { // 장바구니 또는 cartNo로 구매
                items = cartInfoService.getList(cartNo);
            }

            if (items == null || items.isEmpty()) {
                throw new CartItemNotFoundException();
            }

            model.addAttribute("items", items);
            /** 주문서 양식인 경우 장바구니 상품 조회 E */
        }
        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);
    }
}
