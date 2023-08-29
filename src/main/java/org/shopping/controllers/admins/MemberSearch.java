package org.shopping.controllers.admins;

import lombok.Data;

@Data
public class MemberSearch {
    private int page = 1;

    /* 페이지당 회원 수 */
    private int limit = 20;

    /* 검색 조건 */
    private String sopt;

    /* 검색 키워드 */
    private String skey;
}
