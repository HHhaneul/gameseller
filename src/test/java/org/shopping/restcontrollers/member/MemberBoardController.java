package org.shopping.restcontrollers.member;

import lombok.RequiredArgsConstructor;
import org.shopping.models.board.config.BoardConfigSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("apiMemberBoardRestController")
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class MemberBoardController {

    @Autowired
    private BoardConfigSaveService saveService;
}
