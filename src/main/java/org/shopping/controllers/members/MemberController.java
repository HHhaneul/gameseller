package org.shopping.controllers.members;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.shopping.controllers.admins.logins.FindIdForm;
import org.shopping.entities.Member;
import org.shopping.models.member.MemberInfoService;
import org.shopping.models.member.MemberSaveService;
import org.shopping.repositories.member.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberSaveService saveService;
    private final JoinValidator joinValidator;
    private final MemberInfoService infoService;
    private final MemberRepository memberRepository;

    @GetMapping("/join")
    public String join(@ModelAttribute org.shopping.controllers.members.JoinForm joinForm, Model model) {
        commonProcess(model);
        return "member/join";
    }

    @PostMapping("/join")
    public String joinPs(@Valid JoinForm joinForm, Errors errors, Model model) {
        commonProcess(model);

        joinValidator.validate(joinForm, errors);

        if (errors.hasErrors()) {
            return "member/join";
        }

        saveService.save(joinForm);

        return "redirect:/member/login";
    }

    @GetMapping("/login")
    public String login() {

        return "member/login";
    }

    @GetMapping("/findId")
    public String findId(@ModelAttribute FindIdForm findIdForm, Model model) {
        commonProcess(model);
        return "member/findId";
    }

    @PostMapping("/findId")
    public String findIdProcess(@Valid FindIdForm findIdForm, BindingResult bindingResult, Model model) {
        commonProcess(model);

        if (bindingResult.hasErrors()) {
            return "member/findId";
        }

        Member member = memberRepository.findByUserNmAndMobile(findIdForm.getUserNm(), findIdForm.getMobile());

        if (member != null) {
            String userId = member.getUserId().substring(0,member.getUserId().length()-3)+"***";
            findIdForm.setFoundUserId(userId);
            model.addAttribute("message", "찾으시는 아이디는 " + userId + "입니다.");
        } else {
            model.addAttribute("error", "이름 또는 휴대전화번호를 다시 확인해주세요.");
        }

        return "member/findId";
    }


    private void commonProcess(Model model) {
        model.addAttribute("pageTitle", "회원가입");
    }
}