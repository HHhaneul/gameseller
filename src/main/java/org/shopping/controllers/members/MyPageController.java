package org.shopping.controllers.members;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.shopping.commons.Utils;
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

@Controller
@RequestMapping
@RequiredArgsConstructor
public class MyPageController {

    private final MemberSaveService saveService;
    private final JoinValidator joinValidator;
    private final MemberRepository memberRepository;
    private final MemberInfoService infoService;
    private final HttpSession session;
    private final PasswordEncoder passwordEncoder;
    private final Utils utils;

    @GetMapping("/cart/index")
    public String cart_index() {

        return "cart/index";
    }

    @GetMapping("/myPage/main")
    public String myPage_main() {

        return "order/cart";
    }

    @GetMapping("/member/support/inquire")
    public String support() {

        return "myPage/myPage_QnA";
    }

    @GetMapping("/myPage/update")
    public String showUpdateForm(String userId, Model model) {
        MemberInfo memberInfo = (MemberInfo) session.getAttribute("memberInfo");
        System.out.println(memberInfo);
        model.addAttribute("member", memberInfo);

        /* Member member = saveService.findById(userId);
        if (member == null) {
            return "redirect:/";
        }

        model.addAttribute("userId", userId);*/

        return "myPage/update";
    }

    @PostMapping("/update")
    public String updateMember(@ModelAttribute Member updatedMember ) {
        MemberInfo memberInfo = (MemberInfo) session.getAttribute("memberInfo");
        Long userNo = memberInfo.getUserNo();
        Member member = memberRepository.findById(userNo).orElseThrow(MemberNotFoundException::new);



        if (memberInfo == null) {
            return "redirect:/";
        }
        /*
        member.setUserNm(updatedMember.getUserNm());
        member.setEmail(updatedMember.getEmail());
        member.setMobile(updatedMember.getMobile());
        */
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
}
