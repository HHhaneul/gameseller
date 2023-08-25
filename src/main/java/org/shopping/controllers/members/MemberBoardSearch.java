package org.shopping.controllers.members;

import lombok.Data;

@Data
public class MemberBoardSearch {
    private int page = 1;

    /* 페이지당 게시글 수 */
    private int limit = 20;

    /* 검색 조건 */
    private String sopt;

    /* 검색 키워드 */
    private String skey;
}
