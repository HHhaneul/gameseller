package org.shopping.controllers.games;

import lombok.RequiredArgsConstructor;
import org.shopping.CommonProcess;
import org.shopping.commons.AlertBackException;
import org.shopping.commons.CommonException;
import org.shopping.commons.ListData;
import org.shopping.commons.ScriptExceptionProcess;
import org.shopping.entities.Game;
import org.shopping.models.games.GameInfoService;
import org.shopping.models.games.GameSearch;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/game")
@RequiredArgsConstructor
public class GameController implements CommonProcess, ScriptExceptionProcess {
    private final GameInfoService gameInfoService;

    @GetMapping("/list")
    public String list(@ModelAttribute GameSearch gameSearch, Model model) {
        commonProcess(model, "list");

        /* 최신게임 */
        commonProcess(model, "list");
        ListData<Game> data2 = gameInfoService.getList(gameSearch);
        model.addAttribute("items2", data2.getContent());
        model.addAttribute("pagination", data2.getPagination());

        /* 인기게임 */
        ListData<Game> data3 = gameInfoService.getList(gameSearch);
        model.addAttribute("items3", data3.getContent());
        model.addAttribute("pagination", data3.getPagination());

        /* 한글화 게임 */
        ListData<Game> data4 = gameInfoService.getList(gameSearch);
        model.addAttribute("items4", data4.getContent());
        model.addAttribute("pagination", data4.getPagination());

        List<String> icons = new ArrayList<>();
        icons.add("베스트");
        icons.add("인기");
        icons.add("주문폭주");
        icons.add("최다판매");
        icons.add("추천");
        model.addAttribute("icons", icons);

        return "game/list";
    }
    @GetMapping("/view/{gameNo}")
    public String view(@PathVariable Long gameNo, Model model) {
        try {
            Game data = gameInfoService.get(gameNo);
            commonProcess(model, "view", data.getGameNm());

            model.addAttribute("data", data);

        } catch (CommonException e) {
            e.printStackTrace();
            throw new AlertBackException(e.getMessage());
        }
        return "game/view";
    }

    public void commonProcess(Model model, String mode) {
        commonProcess(model, mode, null);
    }

    public void commonProcess(Model model, String mode, String addTitle) {
        String pageTitle = "";
        if (mode.equals("view")) {
            if (addTitle != null && !addTitle.isBlank()) addTitle += "|";
            pageTitle = addTitle;
        }

        CommonProcess.super.commonProcess(model, pageTitle);

        List<String> addScript = new ArrayList<>();
        List<String> addCss = new ArrayList<>();

        if (mode.equals("view")) {
            addScript.add("game/view");
        }


        model.addAttribute("addScript", addScript);
        model.addAttribute("addCss", addCss);
    }
}
