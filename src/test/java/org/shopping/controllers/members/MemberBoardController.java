package org.shopping.controllers.members;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.shopping.entities.MemberBoardData;
import org.shopping.models.member.board.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.shopping.commons.libs.JavaScript.alertBack;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class MemberBoardController {

    private final MemberBoardSaveService saveService;
    private final MemberBoardInfoService infoService;
    private final MemberBoardListService listService;

    private final MemberBoardDeleteService deleteService;

    // 게시글 작성 양식
    @GetMapping("/write")
    public String write(Model model) {
        MemberBoardForm boardForm = new MemberBoardForm();
        model.addAttribute("boardForm", boardForm);
        model.addAttribute("addScript", new String[]{"ckeditor/ckeditor", "form"});
        return "board/write";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, Model model) {

        MemberBoardData memberBoardData = infoService.get(id);

        MemberBoardForm boardForm = new ModelMapper().map(memberBoardData, MemberBoardForm.class);
        model.addAttribute("boardForm", boardForm);
        model.addAttribute("addScript", new String[]{"ckeditor/ckeditor", "form"});

        return "board/update";
    }

    // 게시글 작성 및 수정 처리
    @PostMapping("/save")
    public String save(@Valid MemberBoardForm memberBoardform, Errors errors) {

        try {
            saveService.save(memberBoardform, errors);
        } catch (Exception e) {
            errors.reject("boardSaveErr", e.getMessage());
        }

        if (errors.hasErrors()) {
            Long id = memberBoardform.getId();
            if (id == null) {
                return "board/write";
            } else {
                return "board/update";
            }
        }

        return "redirect:/board/list";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") Long id, Model model) {

        MemberBoardData memberboardData = infoService.get(id);
        model.addAttribute("memberBoardData", memberboardData);

        return "board/view";
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<MemberBoardData> list = listService.gets();

        model.addAttribute("list", list);

        return "board/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        deleteService.delete(id);

        return "redirect:/board/list";
    }


    @ExceptionHandler(Exception.class)
    public String errorHandler(Exception e, Model model) {
        model.addAttribute("scripts", alertBack(e.getMessage()));
        return "commons/execute_script";
    }
}
