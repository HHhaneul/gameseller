package org.shopping.controllers.admins;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.shopping.commons.*;
import org.shopping.controllers.members.MemberBoardSearch;
import org.shopping.entities.Board;
import org.shopping.entities.MemberBoardData;
import org.shopping.models.board.config.BoardConfigInfoService;
import org.shopping.models.board.config.BoardConfigListService;
import org.shopping.models.board.config.BoardConfigSaveService;
import org.shopping.models.member.board.MemberBoardInfoService;
import org.shopping.models.member.board.MemberBoardListService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller("AdminBoardController")
@RequestMapping("/admin/board")
@RequiredArgsConstructor
public class BoardController {

    private final HttpServletRequest request;
    private final BoardConfigSaveService configSaveService;
    private final BoardConfigInfoService boardConfigInfoService;
    private final BoardConfigListService boardConfigListService;

    private final MemberBoardListService memberBoardListService;
    private final MemberBoardInfoService memberBoardInfoService;

    /**
     * 게시판 목록
     *
     * @return
     */
    @GetMapping
    public String index(@ModelAttribute BoardSearch boardSearch, Model model) {
        commonProcess(model, "게시판 목록");

        Page<Board> data = boardConfigListService.gets(boardSearch);
        model.addAttribute("items", data.getContent());

        return "admin/board/index";
    }

    /**
     * 게시판 등록
     * @return
     */
    @GetMapping("/register")
    public String register(@ModelAttribute BoardForm boardForm, Model model) {
        commonProcess(model, "게시판 등록");

        return "admin/board/config";
    }

    @GetMapping("/{bId}/update")
    public String update(@PathVariable String bId, Model model) {
        commonProcess(model, "게시판 수정");

        Board board = boardConfigInfoService.get(bId, true);
        BoardForm boardForm = new ModelMapper().map(board, BoardForm.class);
        boardForm.setMode("update");
        boardForm.setListAccessRole(board.getListAccessRole().toString());
        boardForm.setViewAccessRole(board.getViewAccessRole().toString());
        boardForm.setWriteAccessRole(board.getWriteAccessRole().toString());
        boardForm.setReplyAccessRole(board.getReplyAccessRole().toString());
        boardForm.setCommentAccessRole(board.getCommentAccessRole().toString());

        model.addAttribute("boardForm", boardForm);

        return "admin/board/config";
    }

    @PostMapping("/save")
    public String save(@Valid BoardForm boardForm, Errors errors, Model model) {
        String mode = boardForm.getMode();
        commonProcess(model, mode != null && mode.equals("update") ? "게시판 수정" : "게시판 등록");

        try {
            configSaveService.save(boardForm, errors);
        } catch (CommonException e) {
            errors.reject("BoardConfigError", e.getMessage());
        }

        if (errors.hasErrors()) {
            return "admin/board/config";
        }


        return "redirect:/admin/board"; // 게시판 목록
    }

    @GetMapping("/posts")
    public String list(@ModelAttribute MemberBoardSearch memberBoardSearch, Model model) {

        String URI = request.getRequestURI();
        // 서브 메뉴 처리
        String subMenuCode = GameMenus.getSubMenuCode(request);
        model.addAttribute("subMenuCode", subMenuCode);

        List<MenuDetail> submenus = GameMenus.gets("board");
        model.addAttribute("submenus", submenus);

        model.addAttribute("pageTitle", "전체 게시판");
        model.addAttribute("title", "전체 게시판");

        Page<MemberBoardData> data = memberBoardListService.gets(memberBoardSearch);
        model.addAttribute("items", data.getContent());

        return "admin/board/posts";
    }

    private void commonProcess(Model model, String title) {
        String URI = request.getRequestURI();

        // 서브 메뉴 처리
        String subMenuCode = GameMenus.getSubMenuCode(request);
        model.addAttribute("subMenuCode", subMenuCode);

        List<MenuDetail> submenus = GameMenus.gets("board");
        model.addAttribute("submenus", submenus);

        model.addAttribute("pageTitle", title);
        model.addAttribute("title", title);
    }

    @GetMapping("/{bId}")
    public String list(@ModelAttribute MemberBoardSearch memberBoardSearch, @PathVariable String bId, Model model) {
        search(model, bId);

        Page<MemberBoardData> data = memberBoardListService.gets(memberBoardSearch, bId);
        model.addAttribute("items", data.getContent());

        return "admin/board/list";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") Long id, Model model) {

        MemberBoardData memberboardData = memberBoardInfoService.get(id);
        model.addAttribute("memberBoardData", memberboardData);
        model.addAttribute("pageTitle", memberboardData.getBoard().getBId());


        return "admin/board/view";
    }


    private void search(Model model, String title){

        String URI = request.getRequestURI();
        // 서브 메뉴 처리
        String subMenuCode = GameMenus.getSubMenuCode(request);
        model.addAttribute("subMenuCode", subMenuCode);

        List<MenuDetail> submenus = GameMenus.gets("board");
        model.addAttribute("submenus", submenus);

        model.addAttribute("pageTitle", "게시글 검색");
        model.addAttribute("title", title);
    }
}