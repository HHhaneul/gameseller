package org.shopping.controllers.members;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.shopping.commons.ListData;
import org.shopping.commons.MemberUtil;
import org.shopping.commons.menus.GameMenus;
import org.shopping.commons.menus.MenuDetail;
import org.shopping.entities.Board;
import org.shopping.entities.MemberBoardData;
import org.shopping.models.board.config.BoardConfigInfoService;
import org.shopping.models.member.board.MemberBoardDeleteService;
import org.shopping.models.member.board.MemberBoardInfoService;
import org.shopping.models.member.board.MemberBoardListService;
import org.shopping.models.member.board.MemberBoardSaveService;
import org.shopping.repositories.BoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.shopping.commons.libs.JavaScript.alertBack;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class MemberBoardController {

    private final MemberBoardSaveService saveService;
    private final MemberBoardInfoService infoService;
    private final MemberBoardListService listService;

    private final BoardConfigInfoService configInfoService;

    private final MemberBoardDeleteService deleteService;
    private final MemberUtil memberUtil;
    private final HttpServletRequest request;

    private final BoardSaveValidator saveValidator;
    private final BoardRepository boardRepository;
    private Board board;

    /* 게시글 작성 양식 */
    @GetMapping("/write/{bId}")
    public String write(@PathVariable String bId, @ModelAttribute MemberBoardForm memberBoardForm, Model model) {
        commonProcess(bId, "write", model);

        memberBoardForm.setBId(board.getBId());
        memberBoardForm.setPoster(memberUtil.getMember().getUserNm());

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
    @PostMapping("/save")
    public String save(@Valid MemberBoardForm memberBoardform, Errors errors) {
        saveValidator.validate(memberBoardform, errors);

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
    public String list(@ModelAttribute MemberBoardSearch memberBoardSearch, Model model) {

        search(model, "전체 게시판");

        Page<MemberBoardData> data = listService.gets(memberBoardSearch);
        model.addAttribute("items", data.getContent());

        return "board/index";
    }

    @GetMapping("/{bId}")
    public String list(@ModelAttribute MemberBoardSearch memberBoardSearch, @PathVariable String bId, Model model) {

        search(model, bId);

        String pageTitle = boardRepository.findBybId(bId).getBName();

        model.addAttribute("pageTitle", pageTitle);

        ListData<MemberBoardData> data = listService.gets(memberBoardSearch, bId);
        model.addAttribute("items", data.getContent());
        model.addAttribute("pagination", data.getPagination());
        model.addAttribute("bId", bId);
        return "board/_index";
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        deleteService.delete(id);
        return "redirect:/board/list";
    }

    @ExceptionHandler(Exception.class)
    public String errorHandler(Exception e, Model model) {
        model.addAttribute("scripts", alertBack(e.getMessage()));
        return "commons/_execute_script";
    }

    private void commonProcess(String bId, String action, Model model) {
        /**
         * 1. bId 게시판 설정 조회
         * 2. action - write, update - 공통 스크립트, 공통 CSS
         *           - 에디터 사용 -> 에디터 스크립트 추가
         *           - 에디터 미사용 -> 에디터 스크립트 미추가
         *           - write, list, view -> 권한 체크
         *           - update - 본인이 게시글만 수정 가능
         *                    - 회원 - 회원번호
         *                    - 비회원 - 비회원비밀번호
         *                    - 관리자는 다 가능
         *
         */
        board = configInfoService.get(bId, action);
        List<String> addCss = new ArrayList<>();
        List<String> addScript = new ArrayList<>();
        List<String> addCommonScript = new ArrayList<>();
        List<String> addCommonCss = new ArrayList<>();

        /* 공통 스타일 CSS */
        addCommonCss.add("style");

        /* 글 작성, 수정시 필요한 자바스크립트 */
        if (action.equals("write") || action.equals("update")) {
        addCommonScript.add("ckeditor/ckeditor");
        addCommonScript.add("fileManager");
        addCommonScript.add("form");

        }
        /* 공통 필요 속성 추가 */
        model.addAttribute("board", board); // 게시판 설정
        model.addAttribute("addCss", addCss); // CSS 설정
        model.addAttribute("addScript", addScript); // JS 설정
        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addCommonCss", addCommonCss); // JS 설정
    }

    private void search(Model model, String title){

        String URI = request.getRequestURI();
        // 서브 메뉴 처리
        String subMenuCode = GameMenus.getSubMenuCode(request);
        model.addAttribute("subMenuCode", subMenuCode);

        List<MenuDetail> submenus = GameMenus.gets("board");
        model.addAttribute("submenus", submenus);

        model.addAttribute("pageTitle", title);
        model.addAttribute("title", title);
    }
}
