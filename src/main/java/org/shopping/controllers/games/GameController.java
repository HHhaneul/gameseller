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
    private final GameInfoService infoService;

    @GetMapping("/list")
    public String list(@ModelAttribute GameSearch search, Model model) {
        commonProcess(model, "list");
        ListData<Game> data = infoService.getList(search);
        model.addAttribute("items", data.getContent());
        model.addAttribute("pagination", data.getPagination());
        return "game/list";
    }
    @GetMapping("/view/{gameNo}")
    public String view(@PathVariable Long gameNo, Model model) {
        try {
            Game data = infoService.get(gameNo);
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
