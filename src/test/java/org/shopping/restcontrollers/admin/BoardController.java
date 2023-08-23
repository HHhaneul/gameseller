package org.shopping.restcontrollers.admin;

import lombok.RequiredArgsConstructor;
import org.shopping.controllers.admins.BoardForm;
import org.shopping.models.board.config.BoardConfigSaveService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
