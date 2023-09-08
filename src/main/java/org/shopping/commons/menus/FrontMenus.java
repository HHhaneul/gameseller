package org.shopping.commons.menus;

import jakarta.servlet.http.HttpServletRequest;

import java.util.*;

public class FrontMenus {
    public static List<MenuDetail> gets(String code) {
        List<MenuDetail> menus = new ArrayList<>();
        if (code.equals("notice")){

        } else if (code.equals("cart")) {

        } else if (code.equals("game")) { // 게임 목록 하위 메뉴
            menus.add(new MenuDetail("list", "최신게임", "/game/list"));
            menus.add(new MenuDetail("promotion", "프로모션", "/game/promotion"));
            menus.add(new MenuDetail("steam", "스팀", "/game/steam"));
        } else if (code.equals("support")) { // 고객지원 하위 메뉴
            menus.add(new MenuDetail("inquire", "문의하기", "/support/inquire"));
            menus.add(new MenuDetail("repeatedly", "자주묻는질문", "/support/repeatedly"));
        } else if (code.equals("myPage")){ // 마이페이지 하위 메뉴
            menus.add(new MenuDetail("main", "쇼핑정보", "/member/myPage_main"));
            menus.add(new MenuDetail("order", "주문내역", "/member/myPage_order"));
            menus.add(new MenuDetail("QnA", "문의내역", "/member/myPage_QnA"));
            menus.add(new MenuDetail("update", "정보수정", "/member/update"));
            menus.add(new MenuDetail("leave", "회원탈퇴", "/member/leave"));
        }


        return menus;
    }
    public static String getSubMenuCode(HttpServletRequest request) {
        String URI = request.getRequestURI();

        return URI.substring(URI.lastIndexOf('/') + 1);
    }
}