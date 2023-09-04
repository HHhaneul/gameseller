package org.shopping.controllers.members;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/support")
@RequiredArgsConstructor
public class SupportController {

    @GetMapping("/inquire")
    public String support() {

        return "support/inquire";
    }
}
