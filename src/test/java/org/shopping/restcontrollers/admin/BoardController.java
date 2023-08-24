package org.shopping.restcontrollers.admin;

import lombok.RequiredArgsConstructor;
import org.shopping.controllers.admins.BoardForm;
import org.shopping.models.board.config.BoardConfigSaveService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController("boardRestController")
@RequestMapping("/api/admin/board")
@RequiredArgsConstructor
public class BoardController {

    private BoardConfigSaveService boardConfigSaveService;

    @PostMapping("/register")
    public ResponseEntity<Object> write(@RequestBody BoardForm form){
        boardConfigSaveService.save(form);
        /* 등록 성공 */
        return ResponseEntity.status(HttpStatus.CREATED).build();


    }
}
