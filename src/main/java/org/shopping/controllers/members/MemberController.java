package org.shopping.controllers.members;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.shopping.models.member.MemberSaveService;
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

    @RequestMapping("/idlookup-form")
    public String find_Id() {
        return "member/idlookup-form";
    }

    private void commonProcess(Model model) {
        model.addAttribute("pageTitle", "회원가입");
    }
}