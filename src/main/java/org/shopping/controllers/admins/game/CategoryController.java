package org.shopping.controllers.admins.game;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.shopping.CommonProcess;
import org.shopping.commons.*;
import org.shopping.commons.constants.GameStatus;
import org.shopping.controllers.admins.CategoryForm;
import org.shopping.controllers.members.MemberBoardForm;
import org.shopping.entities.Category;
import org.shopping.models.categories.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller("adminGameCategoryController")
@RequestMapping("/admin/game/category")
@RequiredArgsConstructor
public class CategoryController implements CommonProcess, ScriptExceptionProcess {

    private String tplCommon = "admin/game/";

    private final CategoryInfoService categoryInfoService;
    private final CategorySaveService categorySaveService;
    private final CategoryDeleteService categoryDeleteService;

    private final HttpServletRequest request;


    /**
     * 게임분류 목록
     */
    @GetMapping
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
    @PostMapping
    public String categorySave(CategoryForm form, Model model) {
        commonProcess(model, "category");

        String mode = form.getMode();
        mode = mode == null || mode.isBlank() ? "add" : mode;
        try {
            if (mode.equals("add")) {
                categorySaveService.save(form);

            } else if (mode.equals("edit")) {
                categorySaveService.saveList(form);

            } else if (mode.equals("delete")) {
                categoryDeleteService.deleteList(form);
            }
        } catch (CommonException e) {
            e.printStackTrace();
            /* 자바스크립트 alert 형태로 에러 출력 */
            throw new AlertException(e.getMessage());
        }

        String script = "parent.location.reload();";
        model.addAttribute("script", script);
        return "common/_execute_script";

    }

    @GetMapping
    public String AddCategory(@ModelAttribute CategoryForm categoryForm) {


        return "admin/game/category_add";
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
            model.addAttribute("statusList", GameStatus.getList());
        }

        model.addAttribute("menuCode", "game");
        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addScript", addScript);

        /* 서브 메뉴 처리 */
        String subMenuCode = GameMenus.getSubMenuCode(request);
        model.addAttribute("subMenuCode", subMenuCode);

        /* 서브 메뉴 조회 */
        List<MenuDetail> submenus = GameMenus.gets("game");
        model.addAttribute("submenus", submenus);
    }
}

