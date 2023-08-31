package org.shopping.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.shopping.CommonProcess;
import org.shopping.commons.constants.GameStatus;
import org.shopping.commons.menus.GameMenus;
import org.shopping.commons.menus.MenuDetail;
import org.shopping.controllers.members.MemberBoardSearch;
import org.shopping.entities.MemberBoardData;
import org.shopping.models.member.board.MemberBoardInfoService;
import org.shopping.models.member.board.MemberBoardListService;
import org.springframework.data.domain.Page;
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

    @GetMapping
    public String index(MemberBoardSearch memberBoardSearch, Model model){
        commonProcess(model, "main");

        memberBoardSearch.setLimit(5);
        Page<MemberBoardData> data = memberBoardListService.gets(memberBoardSearch, "notice");
        data.getContent().stream().forEach(System.out::println);
        model.addAttribute("items", data.getContent());
        return "main/index";
    }


    @GetMapping("/games")
    public String games(Model model){
        commonProcess(model, "games");

        return "main/games";
    }

    @GetMapping("/notice")
    public String notice(Model model){
        commonProcess(model, "notice");

        return "main/notice";
    }

    @GetMapping("/support")
    public String support(Model model){
        commonProcess(model, "support");

        return "main/support";
    }

    @GetMapping("/myPage")
    public String myPage(Model model){
        commonProcess(model, "myPage");

        return "main/myPage";
    }


    @GetMapping("/inquire")
    public String inquire(Model model){
        commonProcess(model, "inquire");
        return "main/inquire";
    }

    @Override
    public void commonProcess(Model model, String mode) {


        List<String> addCommonScript = new ArrayList<>();
        List<String> addScript = new ArrayList<>();

        String pageTitle = "메인페이지";
        if (mode.equals("games")) {
            pageTitle = "게임 목록";

        } else if (mode.equals("promotion")) {
            pageTitle = "프로 모션";

        } else if (mode.equals("notice")) {
            pageTitle = "공지 사항";

        } else if (mode.equals("support")) {
            pageTitle = "고객 지원";

        } else if (mode.equals("myPage")) {
            pageTitle = "마이 페이지";

        } else if (mode.equals("inquire")) {
            pageTitle = "문의하기";
        }

        CommonProcess.super.commonProcess(model, pageTitle);

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

