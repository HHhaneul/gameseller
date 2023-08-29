package org.shopping.commons;

import java.util.ArrayList;
import java.util.List;

public class MyPageMenus {
    public static List<PageDetails> gets(String code) {
        List<PageDetails> menus = new ArrayList<>();
        if (code.equals("mypage")) { // 마이페이지 하위 목록
            menus.add(new PageDetails("main", "쇼핑정보", "/member/mypage_main"));
            menus.add(new PageDetails("order", "주문내역", "/member/mypage_order"));
            menus.add(new PageDetails("qna", "문의내역", "/member/mypage_qna"));
            menus.add(new PageDetails("update", "정보수정", "/member/update"));
            menus.add(new PageDetails("leave", "회원탈퇴", "/member/leave"));
        }
        return menus;
    }
}
