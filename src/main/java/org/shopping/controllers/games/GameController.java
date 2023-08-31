package org.shopping.controllers.games;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/game")
public class GameController {

    @GetMapping("/view/{gameNo}")
    public String view(@PathVariable Long gameNo) {


        return "game/view";
    }

}
