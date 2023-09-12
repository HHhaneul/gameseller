package org.shopping.controllers.admins;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.shopping.CommonProcess;
import org.shopping.commons.exception.AlertBackException;
import org.shopping.commons.exception.CommonException;
import org.shopping.commons.ListData;
import org.shopping.commons.ScriptExceptionProcess;
import org.shopping.commons.menus.GameMenus;
import org.shopping.commons.menus.MenuDetail;
import org.shopping.controllers.members.MemberBoardSearch;
import org.shopping.entities.Board;
import org.shopping.entities.MemberBoardData;
import org.shopping.models.board.config.BoardConfigDeleteService;
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

import java.util.ArrayList;
import java.util.List;

@Controller("AdminBoardController")
@RequestMapping("/admin/board")
@RequiredArgsConstructor
public class BoardController implements CommonProcess, ScriptExceptionProcess {

    private final HttpServletRequest request;
    private final BoardConfigSaveService configSaveService;
    private final BoardConfigInfoService boardConfigInfoService;
    private final BoardConfigListService boardConfigListService;
    private final BoardConfigDeleteService boardConfigDeleteService;

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


    @PostMapping
    public String indexPs(@RequestParam("bId") String[] bIds, Model model) {

        boardConfigDeleteService.delete(bIds);

        model.addAttribute("script", "parent.location.reload();");
        return "commons/_exe11cute_script";
    }

    /**
     * 게시판 등록
     * @return
     */
    @GetMapping("/register")
    public String register(@ModelAttribute BoardForm boardForm, Model model) {
        commonProcess(model, "add");

        return "admin/board/config";
    }

    @GetMapping("/{bId}/update")
    public String update(@PathVariable String bId, Model model) {
        commonProcess(model, "edit");

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



    @GetMapping("/{bId}")
    public String list(@ModelAttribute MemberBoardSearch memberBoardSearch, @PathVariable String bId, Model model) {
        ListData<MemberBoardData> data = memberBoardListService.gets(memberBoardSearch, bId);
        String pageTitle = boardConfigInfoService.get(bId).getBName();
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("items", data.getContent());

        return "admin/board/list";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") Long id, Model model) {

        MemberBoardData memberboardData = memberBoardInfoService.get(id);
        model.addAttribute("memberBoardData", memberboardData);
        model.addAttribute("pageTitle", memberboardData.getBoard().getBName());

        String menuCode = GameMenus.getSubMenuCode(request);
        System.out.println("메뉴코드" + menuCode);

        model.addAttribute("menuCode", "board");
        // 서브 메뉴 처리
        model.addAttribute("subMenuCode", "posts");

        // 서브 메뉴 조회
        List<MenuDetail> submenus = GameMenus.gets("board");
        model.addAttribute("submenus", submenus);


        return "admin/board/view";
    }

    /* 개별 게시판 삭제 */
    @PostMapping("/{bId}/delete")
    public String delete(@PathVariable String bId){
        try {
            boardConfigDeleteService.delete(bId);
        } catch (CommonException e) {
            e.printStackTrace();
            throw new AlertBackException(e.getMessage());
        }
        return "redirect:/admin/board";
    }

    @PostMapping("/delete")
    public String listDelete(@PathVariable String bId, Model model){
        try {
            boardConfigDeleteService.delete(bId);
        } catch (CommonException e) {
            e.printStackTrace();
            throw new AlertBackException(e.getMessage());
        }
        return "commons/_execute_script";
    }

    @Override
    public void commonProcess(Model model, String mode) {
        String pageTitle = "게시판 목록";
        if (mode.equals("add")) {
            pageTitle = "게시판 등록";
        } else if (mode.equals("edit")) {
            pageTitle = "게시판 수정";
        } else if (mode.equals("list")) {
            pageTitle = "게시글 목록";
        }

        CommonProcess.super.commonProcess(model, pageTitle);

        List<String> addCommonScript = new ArrayList<>();
        List<String> addScript = new ArrayList<>();
        if (mode.equals("add") || mode.equals("edit") || mode.equals("save")) {
            addCommonScript.add("ckeditor/ckeditor");
            addCommonScript.add("fileManager");
            addScript.add("game/form");
        }

        String menuCode = GameMenus.getSubMenuCode(request);

        model.addAttribute("menuCode", menuCode);
        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);

        // 서브 메뉴 처리
        model.addAttribute("subMenuCode", menuCode);

        // 서브 메뉴 조회
        List<MenuDetail> submenus = GameMenus.gets("board");
        model.addAttribute("submenus", submenus);
    }
}