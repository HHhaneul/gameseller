package org.shopping.commons.menus;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

public class GameMenus {
    public static List<MenuDetail> gets(String code) {
        List<MenuDetail> menus = new ArrayList<>();

        // 회원 하위 메뉴
        if (code.equals("member")) {
            menus.add(new MenuDetail("member", "회원 목록", "/admin/member"));
        } else if (code.equals("adminGame")) { // 상품관리 하위 메뉴
            menus.add(new MenuDetail("game", "게임 목록", "/admin/game"));
            menus.add(new MenuDetail("add", "게임 등록", "/admin/game/add"));
            menus.add(new MenuDetail("category", "게임 분류", "/admin/game/category"));
        } else if (code.equals("buyer")) { // 주문관리 하위 메뉴
            menus.add(new MenuDetail("list", "주문 목록", "/admin/buyer/list"));
            menus.add(new MenuDetail("view", "주문 보기", "/admin/buyer/list"));
        } else if (code.equals("board")) { // 게시판 하위 메뉴
            menus.add(new MenuDetail("board", "게시판 목록", "/admin/board"));
            menus.add(new MenuDetail("register", "게시판 등록/수정", "/admin/board/register"));
            menus.add(new MenuDetail("posts", "게시글 관리", "/admin/board/posts"));
        } else if (code.equals("order")) {
            menus.add(new MenuDetail("order", "주문 목록", "/admin/order"));
        }

        return menus;
    }

    public static String getSubMenuCode(HttpServletRequest request) {
        String URI = request.getRequestURI();

        return URI.substring(URI.lastIndexOf('/') + 1);
    }
}

