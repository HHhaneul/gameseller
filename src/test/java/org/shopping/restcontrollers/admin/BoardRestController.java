package org.shopping.restcontrollers.admin;

import lombok.RequiredArgsConstructor;
import org.shopping.controllers.admins.BoardForm;
import org.shopping.models.board.config.BoardConfigSaveService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController("apiBoardRestController")
@RequestMapping("/api/admin/board")
@RequiredArgsConstructor
public class BoardRestController {

    private final BoardConfigSaveService saveService;

    @PostMapping("/register")
    public ResponseEntity<Object> save(@RequestBody BoardForm form){
        saveService.save(form);

        /* 등록 성공 */
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
