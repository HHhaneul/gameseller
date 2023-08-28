package org.shopping.controllers.members;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.shopping.controllers.logins.FindIdForm;
import org.shopping.controllers.logins.FindIdView;
import org.shopping.entities.Member;
import org.shopping.models.member.MemberInfo;
import org.shopping.models.member.MemberInfoService;
import org.shopping.models.member.MemberSaveService;
import org.shopping.repositories.member.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberSaveService saveService;
    private final JoinValidator joinValidator;
    private final MemberInfoService infoService;
    private final MemberRepository memberRepository;

    @GetMapping("/join")
    public String join(@ModelAttribute JoinForm joinForm, Model model) {
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
    @GetMapping("/idlookup-form")
    public String findId(@ModelAttribute FindIdForm findIdForm, Member member, Model model, String mobile) {

        return "member/idlookup-form";
    }
    @GetMapping("/idlookup-result")
    public String findIdView(@Valid FindIdForm findIdForm, MemberInfo info, Model model, String mobile){

        if (mobile == findIdForm.getMobile()){
            info = (MemberInfo) infoService.loadUserByUsername(findIdForm.getUserNm());
        }
        System.out.println("인포" + info);
        return "member/idlookup-result";
    }

    @GetMapping("/idlookup-result/{userId}")
    public String findIdview(@PathVariable("userId") String userId, Model model) {
        Member member = memberRepository.findByUserId(userId);
        if (member == null) {
            return "redirect:/member/join";
        }
        model.addAttribute("member", member);
        return "member/idlookup-form";
    }

    private void commonProcess(Model model) {
        model.addAttribute("pageTitle", "회원가입");
    }
}