package org.shopping.controllers.members;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.shopping.commons.CommonException;
import org.shopping.commons.Utils;
import org.shopping.entities.Member;
import org.shopping.models.member.MemberInfo;
import org.shopping.models.member.MemberInfoService;
import org.shopping.models.member.MemberNotFoundException;
import org.shopping.models.member.MemberSaveService;


import org.shopping.repositories.member.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final MemberRepository memberRepository;
    private final MemberInfoService infoService;
    private final HttpSession session;
    private final PasswordEncoder passwordEncoder;
    private final Utils utils;


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

    @GetMapping("/myPage")
    public String myPage() {

        return "member/myPage";
    }

    private void commonProcess(Model model) {
        model.addAttribute("pageTitle", "회원가입");
    }

    @GetMapping("/update")
    public String showUpdateForm(String userId, Model model) {
        MemberInfo memberInfo = (MemberInfo) session.getAttribute("memberInfo");
        model.addAttribute("member", memberInfo);

        return "member/update";
    }

    @PostMapping("/update")
    public String updateMember(@ModelAttribute Member updatedMember ) {
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

    @GetMapping("/changePassword")
    public String showChangePasswordForm(Model model) {
        PasswordChangeForm passwordChangeForm = new PasswordChangeForm();
        passwordChangeForm.setUserId(getLoggedInUserId());
        model.addAttribute("passwordChangeForm", passwordChangeForm);
        return "member/changePassword";
    }

    @PostMapping("/updatePassword")
    public String updatePassword(@Valid PasswordChangeForm passwordChangeForm, Errors errors, Model model) {

        if (errors.hasErrors()) {
            return "member/changePassword";
        }


        Member currentMember = saveService.findById(passwordChangeForm.getUserId());
        if (currentMember == null) {
            return "redirect:/";
        }


        if (!passwordEncoder.matches(passwordChangeForm.getCurrentPassword(), currentMember.getUserPw())) {
            errors.rejectValue("currentPassword", "password.invalid", "현재 비밀번호가 올바르지 않습니다.");
            return "member/changePassword";
        }
        if (passwordChangeForm.getCurrentPassword().equals(passwordChangeForm.getNewPassword())){
            throw new CommonException("samePassword", HttpStatus.BAD_REQUEST);
        }

        // Update the password
        currentMember.setUserPw(passwordEncoder.encode(passwordChangeForm.getNewPassword()));
        saveService.save(currentMember);

        return "redirect:/member/logout";
    }

    private String getLoggedInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getName();
        }
        return null;
    }
}



