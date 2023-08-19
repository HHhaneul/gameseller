package org.shopping.controllers.admins;

import lombok.Data;

/**
 * 게시판 설정 검색
 *
 */
@Data
public class BoardSearch {
    private int page = 1;

    /* 페이지당 게시글 수 */
    private int limit = 20;

    /* 검색 조건 */
    private String sopt;

    /* 검색 키워드 */
    private String skey;
}