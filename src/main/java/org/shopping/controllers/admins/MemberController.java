package org.shopping.controllers.admins;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.Errors;
import org.shopping.commons.MenuDetail;
import org.shopping.commons.Menus;
import org.shopping.controllers.members.JoinValidator;
import org.shopping.entities.Member;
import org.shopping.models.member.MemberInfoService;
import org.shopping.models.member.MemberListService;
import org.shopping.models.member.MemberSaveService;
import org.shopping.repositories.member.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller("AdminMemberController")
@RequestMapping("/admin/member")
@RequiredArgsConstructor
public class MemberController {

    private final HttpServletRequest request;
    private final MemberInfoService memberInfoService;
    private final MemberSaveService memberSaveService;
    private final MemberListService memberListService;
    private final JoinValidator joinValidator;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    /**
     * 회원 목록
     *
     * @return
     */

    @GetMapping
    public String index(@ModelAttribute MemberSearch memberSearch, Model model) {
        commonProcess(model, "회원 목록");

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

    @GetMapping("/edit/{userId}")
    public String showUpdateForm(@PathVariable("userId") String userId, Model model) {
        Member member = memberSaveService.findById(userId);
        if (member == null) {
            return "redirect:/";
        }
        model.addAttribute("member", member);
        model.addAttribute("userId", userId);
        return "member/update";
    }

    @PostMapping("/update")
    public String updateMember(@ModelAttribute Member updatedMember) {
        Member existingMember = memberSaveService.findById(updatedMember.getUserId());
        if (existingMember == null) {
            return "redirect:/";
        }
        existingMember.setUserNm(updatedMember.getUserNm());
        existingMember.setEmail(updatedMember.getEmail());
        existingMember.setMobile(updatedMember.getMobile());
        memberSaveService.save(existingMember);
        return "redirect:/";
    }

    @GetMapping("/changePassword")
    public String showChangePasswordForm(Model model) {
        PasswordChangeForm passwordChangeForm = new PasswordChangeForm();
        passwordChangeForm.setUserId(getLoggedInUserId());
        model.addAttribute("passwordChangeForm", passwordChangeForm);
        return "admin/member/changePassword";
    }

    @PostMapping("/updatePassword")
    public String updatePassword(@Valid PasswordChangeForm passwordChangeForm, Errors errors, Model model) {

        if (errors.hasErrors()) {
            return "member/changePassword";
        }


        Member currentMember = memberSaveService.findById(passwordChangeForm.getUserId());
        if (currentMember == null) {
            return "redirect:/";
        }


        if (!passwordEncoder.matches(passwordChangeForm.getCurrentPassword(), currentMember.getUserPw())) {
            errors.rejectValue("currentPassword", "password.invalid", "현재 비밀번호가 올바르지 않습니다.");
            return "admin/member/changePassword";
        }

        // Update the password
        currentMember.setUserPw(passwordEncoder.encode(passwordChangeForm.getNewPassword()));
        memberSaveService.save(currentMember);

        return "redirect:admin/member/logout";
    }

    private String getLoggedInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getName();
        }
        return null;
    }
}

