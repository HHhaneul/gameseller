package org.shopping.controllers.admins;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.shopping.CommonProcess;
import org.shopping.commons.exception.AlertBackException;
import org.shopping.commons.exception.CommonException;
import org.shopping.commons.menus.GameMenus;
import org.shopping.commons.menus.MenuDetail;
import org.shopping.controllers.members.JoinForm;
import org.shopping.entities.Member;
import org.shopping.models.member.MemberDeleteService;
import org.shopping.models.member.MemberListService;
import org.shopping.models.member.MemberNotFoundException;
import org.shopping.models.member.MemberSaveService;
import org.shopping.repositories.member.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller("AdminMemberController")
@RequestMapping("/admin/member")
@RequiredArgsConstructor
public class AdminMemberController implements CommonProcess {

    private final HttpServletRequest request;
    private final MemberSaveService memberSaveService;
    private final MemberListService memberListService;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final MemberDeleteService memberDeleteService;

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


    public void commonProcess(Model model, String title) {
        String URI = request.getRequestURI();

        model.addAttribute("menuCode", "member");
        // 서브 메뉴 처리
        String subMenuCode = GameMenus.getSubMenuCode(request);
        model.addAttribute("subMenuCode", subMenuCode);

        List<MenuDetail> submenus = GameMenus.gets("member");
        model.addAttribute("submenus", submenus);

        model.addAttribute("pageTitle", title);
        model.addAttribute("title", title);
    }

    /*
        @GetMapping("/changePassword/{userId}")
        public String showUpdateForm(@PathVariable("userId") String userId, Model model) {
            Member member = memberSaveService.findById(userId);
            if (member == null) {
                return "redirect:/";
            }
            model.addAttribute("member", member);
            model.addAttribute("userId", userId);
            return "admin/member/update";
        }
    */
    @GetMapping("/{userNo}/update")
    public String update(@PathVariable Long userNo, @ModelAttribute JoinForm joinForm, Model model) {
        Member member = memberRepository.findById(userNo).orElseThrow(MemberNotFoundException::new);

        joinForm.setMode("update");
        joinForm.setUserId(member.getUserId());
        joinForm.setMobile(member.getMobile());
        joinForm.setUserNm(member.getUserNm());
        joinForm.setEmail(member.getEmail());
        joinForm.setUserPwRe(passwordEncoder.encode(member.getUserPw()));
        joinForm.setUserPw(passwordEncoder.encode(member.getUserPw()));

        model.addAttribute("joinForm", joinForm);

        return "admin/member/update";
    }

    @PostMapping("/save")
    public String updateMember( @Valid JoinForm joinForm, Errors errors) {

        memberSaveService.edit(joinForm);


        return "redirect:/admin/member";

}
    /* 회원 목록 삭제 */
    @PostMapping("{userNo}/delete")
    public String delete(@PathVariable Long userNo) {
        try {
            memberDeleteService.delete(userNo);
        } catch (CommonException e) {
            e.printStackTrace();
            throw new AlertBackException(e.getMessage());
        }
        return "redirect:/admin/member/index";
    }

    @PostMapping("/delete")
    public String listDelete(Long[] userNos, Model model) {

            memberDeleteService.delete(userNos);


        return "commons/_execute_script";
    }




/*
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
            return "admin/member/changePassword";
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
*/

    /*private String getLoggedInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            return authentication.getName();
        }
        return null;
    }*/
    }


