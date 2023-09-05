package org.shopping.commons;


import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
import java.util.List;

public class MainMenus {
    public static List<PageDetails> gets(String code) {
        List<PageDetails> menus = new ArrayList<>();
        if (code.equals("main")) { // 메인페이지 하위 목록
            menus.add(new PageDetails("game", "게임", "/member/game_thumb"));
            menus.add(new PageDetails("promotion", "프로모션", "/member/promotion"));
            menus.add(new PageDetails("notice", "공지사항", "/member/notice"));
            menus.add(new PageDetails("support", "고객지원", "/member/support"));
            menus.add(new PageDetails("mypage", "마이페이지", "/member/mypage"));
            menus.add(new PageDetails("basket", "장바구니", "/member/basket"));
        } else if (code.equals("game")) { // 게임 하위 목록
            menus.add(new PageDetails("steam", "스팀", "/member/steam"));
        } else if (code.equals("support")){
            menus.add(new PageDetails("inquire", "문의하기", "/member/inquire"));
            menus.add(new PageDetails("repeatedly", "자주묻는질문", "/member/repeatedly"));
        }


        return menus;
    }
}
