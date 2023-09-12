package org.shopping.controllers.members;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.shopping.CommonProcess;
import org.shopping.commons.Utils;
import org.shopping.commons.menus.FrontMenus;
import org.shopping.commons.menus.MenuDetail;
import org.shopping.entities.Member;
import org.shopping.models.member.MemberInfo;
import org.shopping.models.member.MemberInfoService;
import org.shopping.models.member.MemberNotFoundException;
import org.shopping.models.member.MemberSaveService;
import org.shopping.repositories.member.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/myPage")
@RequiredArgsConstructor
public class MyPageController implements CommonProcess {

    private final MemberSaveService saveService;
    private final JoinValidator joinValidator;
    private final MemberRepository memberRepository;
    private final MemberInfoService infoService;
    private final HttpSession session;
    private final PasswordEncoder passwordEncoder;
    private final Utils utils;
    private final HttpServletRequest request;


    @GetMapping("/leave")
    public String myPage_leave(Model model) {

        commonProcess(model, "leave");
        return "myPage/leave";
    }


    @GetMapping
    public String myPage_index(Model model) {

        commonProcess(model, "main");
        return "myPage/index";
    }

    @GetMapping("/myPage_QnA")
    public String myPage_QnA(Model model) {

        commonProcess(model, "QnA");
        return "myPage/myPage_QnA";
    }

/*    @GetMapping("/support/inquire")
    public String myPage_inquire() {

        return "support/inquire";
    }*/


    @GetMapping("/update")
    public String showUpdateForm(String userId, Model model) {
        commonProcess(model, "update");
        MemberInfo memberInfo = (MemberInfo) session.getAttribute("memberInfo");
        System.out.println(memberInfo);
        model.addAttribute("member", memberInfo);



        return "myPage/update";
    }

    @PostMapping("/update")
    public String updateMember(@ModelAttribute Member updatedMember, Model model) {
        commonProcess(model, "update");
        MemberInfo memberInfo = (MemberInfo) session.getAttribute("memberInfo");
        Long userNo = memberInfo.getUserNo();
        Member member = memberRepository.findById(userNo).orElseThrow(MemberNotFoundException::new);



        if (memberInfo == null) {
            return "redirect:/";
        }

        ((MemberInfo) session.getAttribute("memberInfo")).setEmail(utils.getParam("email_"));
        ((MemberInfo) session.getAttribute("memberInfo")).setUserNm(utils.getParam("userNm_"));
        ((MemberInfo) session.getAttribute("memberInfo")).setMobile(utils.getParam("mobile_"));
        member.setEmail(utils.getParam("email_"));
        member.setUserNm(utils.getParam("userNm_"));
        member.setMobile(utils.getParam("mobile_"));
        System.out.println("이메일" + utils.getParam("email_"));
        saveService.save(member);
        return "redirect:/";
    }

    @Override
    public void commonProcess(Model model, String mode) {

        String pageTitle = "";
        if (mode == "main"){
            pageTitle = "쇼핑정보";

        } else if (mode == "order") {
            pageTitle = "주문내역";

        } else if (mode == "QnA") {
            pageTitle = "문의내역";

        } else if (mode == "update") {
            pageTitle = "정보수정";

        } else if (mode == "leave") {
            pageTitle = "회원탈퇴";

        }

        List<String> addCommonScript = new ArrayList<>();
        List<String> addScript = new ArrayList<>();

        addScript.add("support");


        String menuCode = FrontMenus.getSubMenuCode(request);
        model.addAttribute("menuCode", "myPage");
        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);

        // 서브 메뉴 처리
        model.addAttribute("menuCode", menuCode);

        // 서브 메뉴 조회
        List<MenuDetail> submenus = FrontMenus.gets("myPage");
        model.addAttribute("submenus", submenus);

        CommonProcess.super.commonProcess(model, pageTitle);
    }
}
