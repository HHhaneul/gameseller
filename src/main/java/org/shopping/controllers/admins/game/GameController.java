package org.shopping.controllers.admins.game;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.shopping.CommonProcess;
import org.shopping.commons.AlertException;
import org.shopping.commons.CommonException;
import org.shopping.commons.ListData;
import org.shopping.commons.ScriptExceptionProcess;
import org.shopping.commons.constants.GameStatus;
import org.shopping.commons.menus.GameMenus;
import org.shopping.commons.menus.MenuDetail;
import org.shopping.entities.Category;
import org.shopping.entities.Game;
import org.shopping.models.categories.CategoryDeleteService;
import org.shopping.models.categories.CategoryInfoService;
import org.shopping.models.categories.CategorySaveService;
import org.shopping.models.games.GameDeleteService;
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
    private final GameDeleteService deleteService;

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
    public String indexPs(GameForm form, Model model) {
        commonProcess(model, "list");

        String mode = form.getMode();
        mode = mode == null || mode.isBlank() ? "add" : mode;
        try {
            if (mode.equals("add")) {
                System.out.println("추가");
                saveService.save(form);
                /* 수정 */
            } else if (mode.equals("edit")) {
                saveService.saveList(form);

                /* 삭제 */
            } else if (mode.equals("delete")) {
                System.out.println("삭제");
                deleteService.deleteList(form);
            }
            /* 등록 */
        } catch (CommonException e) {
            e.printStackTrace();
            throw new AlertException(e.getMessage()); // 자바스크립트 alert 형태로 에러 출력
        }


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
        Game game = infoService.get(gameNo);
        GameForm gameForm = new ModelMapper().map(game, GameForm.class);
        gameForm.setMode("edit");
        gameForm.setCateCd(game.getCategory().getCateCd());
        gameForm.setStatus(game.getStatus().toString());
        gameForm.setGameNm(game.getGameNm());
        gameForm.setPrice(game.getPrice());
        gameForm.setStock(game.getStock());
        gameForm.setListOrder(game.getListOrder());
        gameForm.setDescription(game.getDescription());
        gameForm.setMainImages(game.getMainImages());
        gameForm.setEditorImages(game.getEditorImages());


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
        commonProcess(model, "분류");
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
            if (mode.equals("add")) {
                System.out.println("추가");
                categorySaveService.save(form);
            /* 수정 */
            } else if (mode.equals("edit")) {
                System.out.println("수정");
                categorySaveService.saveList(form);
            /* 삭제 */
            } else if (mode.equals("delete")) {
                System.out.println("삭제");
                categoryDeleteService.deleteList(form);
            }
            /* 등록 */
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
        } else if (mode.equals("분류")) {
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
        List<MenuDetail> submenus = GameMenus.gets("adminGame");
        model.addAttribute("submenus", submenus);
    }


}
