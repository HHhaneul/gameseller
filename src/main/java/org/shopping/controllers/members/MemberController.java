package org.shopping.controllers.members;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.shopping.CommonProcess;
import org.shopping.commons.exception.CommonException;
import org.shopping.commons.Utils;
import org.shopping.controllers.admins.logins.FindIdForm;
import org.shopping.entities.Member;
import org.shopping.models.member.*;
import org.shopping.repositories.member.MemberRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController implements CommonProcess {


    private final MemberSaveService saveService;
    private final JoinValidator joinValidator;
    private final MemberRepository memberRepository;
    private final MemberInfoService infoService;
    private final HttpSession session;
    private final PasswordEncoder passwordEncoder;
    private final Utils utils;
    private final MemberDeleteService memberDeleteService;

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

    @GetMapping("/edit")
    public String showUpdateForm( Model model) {
        MemberInfo memberInfo = (MemberInfo) session.getAttribute("memberInfo");
        System.out.println(memberInfo);
        model.addAttribute("member", memberInfo);

        /* Member member = saveService.findById(userId);
        if (member == null) {
            return "redirect:/";
        }

        model.addAttribute("userId", userId);*/

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

        /*
         * 강사님에게 현재 비밀번호와 바꾸려는 비밀번호가 일치하면 오류나오는거 여쭤보기.
         * utils.getParam() 사용해봤지만 null값이 가져와짐.
         * 알아서 해결해버림
         * */

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