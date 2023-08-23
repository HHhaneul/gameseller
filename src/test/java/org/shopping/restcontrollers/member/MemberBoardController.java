package org.shopping.restcontrollers.member;

import lombok.RequiredArgsConstructor;
import org.shopping.controllers.admins.BoardForm;
import org.shopping.controllers.admins.BoardSearch;
import org.shopping.entities.Board;
import org.shopping.models.board.config.BoardConfigSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController("apiMemberBoardRestController")
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class MemberBoardController {

    @Autowired
    private BoardConfigSaveService saveService;



}
