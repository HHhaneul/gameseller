package org.shopping.controllers.admins;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.shopping.commons.MenuDetail;
import org.shopping.commons.Menus;
import org.shopping.entities.Member;
import org.shopping.models.member.MemberInfoService;
import org.shopping.models.member.MemberListService;
import org.shopping.models.member.MemberSaveService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller("AdminMemberController")
@RequestMapping("/admin/member")
@RequiredArgsConstructor
public class MemberController {

    private final HttpServletRequest request;
    private final MemberInfoService memberInfoService;
    private final MemberSaveService memberSaveService;
    private final MemberListService memberListService;
    /**
     * 회원 목록
     *
     * @return
     */

    @GetMapping
    public String index(@ModelAttribute MemberSearch memberSearch, Model model){
        commonProcess(model,"회원 목록");

        Page<Member> data = memberListService.gets(memberSearch);
        model.addAttribute("items", data.getContent());

        return "admin/member/index";
    }


    private void commonProcess(Model model, String title) {
        String URI = request.getRequestURI();

        // 서브 메뉴 처리
        String subMenuCode = Menus.getSubMenuCode(request);
        model.addAttribute("subMenuCode", subMenuCode);

        List<MenuDetail> submenus = Menus.gets("member");
        model.addAttribute("submenus", submenus);

        model.addAttribute("pageTitle", title);
        model.addAttribute("title", title);
    }
}
