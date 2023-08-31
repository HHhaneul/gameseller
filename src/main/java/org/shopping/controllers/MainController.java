package org.shopping.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class MainController {


    @GetMapping("/games")
    public String games(){

        return "main/games";
    }

    @GetMapping("/notice")
    public String notice(){

        return "main/promotion";
    }

    @GetMapping("/support")
    public String support(){

        return "main/support";
    }

    @GetMapping("/myPage")
    public String myPage(){

        return "member/myPage";
    }

    @GetMapping("/game")
    public String game(){

        return "main/game";
    }

    @GetMapping("/inquire")
    public String inquire(){

        return "main/inquire";
    }
}
