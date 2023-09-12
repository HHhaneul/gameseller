package org.shopping.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.shopping.CommonProcess;
import org.shopping.commons.ListData;
import org.shopping.controllers.members.MemberBoardSearch;
import org.shopping.entities.Game;
import org.shopping.entities.MemberBoardData;
import org.shopping.models.games.GameInfoService;
import org.shopping.models.games.GameSearch;
import org.shopping.models.member.board.MemberBoardListService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class MainController implements CommonProcess {


    private final HttpServletRequest request;
    private final MemberBoardListService memberBoardListService;
    private final GameInfoService gameInfoService;

    @GetMapping
    public String index(MemberBoardSearch memberBoardSearch, GameSearch gameSearch, Model model){

        memberBoardSearch.setLimit(5);
        ListData<MemberBoardData> data = memberBoardListService.gets(memberBoardSearch, "notice");
        model.addAttribute("items", data.getContent());

        /* 최신게임 */
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

        return "main/index";
    }


}

