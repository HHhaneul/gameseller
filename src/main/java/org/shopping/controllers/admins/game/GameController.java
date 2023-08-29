package org.shopping.controllers.admins.game;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.shopping.CommonProcess;
import org.shopping.commons.*;
import org.shopping.commons.constants.GameStatus;
import org.shopping.entities.Category;
import org.shopping.entities.Game;
import org.shopping.models.categories.CategoryDeleteService;
import org.shopping.models.categories.CategoryInfoService;
import org.shopping.models.categories.CategorySaveService;
import org.shopping.models.games.GameInfoService;
import org.shopping.models.games.GameSaveService;
import org.shopping.models.games.GameSearch;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller("adminGameController")
@RequestMapping("/admin/game")
@RequiredArgsConstructor
public class GameController implements CommonProcess, ScriptExceptionProcess {

    private String tplCommon = "admin/game/";
    private final GameSaveService saveService;
    private final GameInfoService infoService;

    private final CategoryInfoService categoryInfoService;
    private final CategorySaveService categorySaveService;
    private final CategoryDeleteService categoryDeleteService;

    private final HttpServletRequest request;

    /**
     * 게임 목록
     *
     * @return
     */
    @GetMapping
    public String index(@ModelAttribute GameSearch search, Model model) {
        commonProcess(model, "list");
        ListData<Game> data = infoService.getList(search);
        model.addAttribute("items", data.getContent());
        model.addAttribute("pagination", data.getPagination());

        return tplCommon + "index";
    }

    /**
     * 게임 목록 수정, 삭제
     *
     */
    @PostMapping
    public String indexPs(Model model) {
        commonProcess(model, "list");


        String script = "parent.location.reload();";
        model.addAttribute("script", script);
        return "commons/_execute_script";
    }

    /**
     * 게임 등록
     *
     */
    @GetMapping("/add")
    public String add(@ModelAttribute GameForm gameForm, Model model) {
        commonProcess(model, "add");
        return tplCommon + "add";
    }

    /**
     * 게임 수정
     *
     */
    @GetMapping("/edit/{gameNo}")
    public String edit(@PathVariable Long gameNo, Model model) {
        commonProcess(model, "edit");
        GameForm gameForm = infoService.getGameForm(gameNo);
        model.addAttribute("gameForm", gameForm);

        return tplCommon + "edit";
    }

    /**
     * 게임 등록/수정 처리
     *
     */
    @PostMapping("/save")
    public String gameSave(@Valid GameForm gameForm, Errors errors, Model model) {
        commonProcess(model, "save");

        String mode = gameForm.getMode();
        if (errors.hasErrors()) {
            return mode != null && mode.equals("edit") ? tplCommon + "edit" : tplCommon + "add";
        }

        saveService.save(gameForm);

        return "redirect:/admin/game";
    }

    /**
     * 게임분류 목록
     */
    @GetMapping("/category")
    public String category(Model model) {
        commonProcess(model, "category");
        List<Category> items = categoryInfoService.getListAll();
        model.addAttribute("items", items);

        return tplCommon + "category";
    }

    /**
     * 게임분류 추가, 수정, 삭제 처리
     *
     */
    @PostMapping("/category")
    public String categorySave(CategoryForm form, Model model) {
        commonProcess(model, "category");

        String mode = form.getMode();
        mode = mode == null || mode.isBlank() ? "add" : mode;
        try {
            if (mode.equals("add")) { // 등록
                categorySaveService.save(form);

            } else if (mode.equals("edit")) { // 수정
                categorySaveService.saveList(form);

            } else if (mode.equals("delete")) { // 삭제
                categoryDeleteService.deleteList(form);
            }
        } catch (CommonException e) {
            e.printStackTrace();
            throw new AlertException(e.getMessage()); // 자바스크립트 alert 형태로 에러 출력
        }

        String script = "parent.location.reload();";
        model.addAttribute("script", script);
        return "commons/_execute_script";

    }

    @Override
    public void commonProcess(Model model, String mode) {


        String pageTitle = "게임 목록";
        if (mode.equals("add")) {
            pageTitle = "게임 등록";
        } else if (mode.equals("edit")) {
            pageTitle = "게임 수정";
        } else if (mode.equals("category")) {
            pageTitle = "게임 분류";
        }

        CommonProcess.super.commonProcess(model, pageTitle);

        List<String> addCommonScript = new ArrayList<>();
        List<String> addScript = new ArrayList<>();
        if (mode.equals("add") || mode.equals("edit") || mode.equals("save")) {
            addCommonScript.add("ckeditor/ckeditor");
            addCommonScript.add("fileManager");
            addScript.add("game/form");
            model.addAttribute("categories", categoryInfoService.getListAll());
        } else if (mode.equals("list")) {
            model.addAttribute("categories", categoryInfoService.getListAll());
            model.addAttribute("statusList", GameStatus.getList());

        }

        model.addAttribute("menuCode", "game");
        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);

        // 서브 메뉴 처리
        String subMenuCode = GameMenus.getSubMenuCode(request);
        model.addAttribute("subMenuCode", subMenuCode);

        // 서브 메뉴 조회
        List<MenuDetail> submenus = GameMenus.gets("game");
        model.addAttribute("submenus", submenus);
    }
}
