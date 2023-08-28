package org.shopping.controllers.admins;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller("adminMainController")
@RequestMapping("/admin")
public class MainController {

    @GetMapping
    public String index() {
        return "admin/main/index";
    }
}