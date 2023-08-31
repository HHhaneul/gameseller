package org.shopping.controllers.admins;

import jakarta.servlet.http.HttpSession;
import org.shopping.models.member.MemberInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller("adminMainController")
@RequestMapping("/admin")
public class MainController {

    @Autowired
    private HttpSession session;

    @GetMapping
    public String index() {

        MemberInfo memberInfo = (MemberInfo)session.getAttribute("memberInfo");

        System.out.println(memberInfo);

        return "admin/main/index";
    }
}