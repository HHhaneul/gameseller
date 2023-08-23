package org.shopping.restcontrollers.member;

import lombok.RequiredArgsConstructor;
import org.shopping.controllers.members.JoinForm;
import org.shopping.models.member.MemberSaveService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController("membercontroller")
@RequestMapping("/api/member/join")
@RequiredArgsConstructor
public class MemberController {

    private final MemberSaveService saveService;

    private JoinForm joinForm;

    @PostMapping
    public ResponseEntity<Object> joinPs(@RequestBody JoinForm form){
        saveService.save(form);

        /* 등록 성공 */
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
