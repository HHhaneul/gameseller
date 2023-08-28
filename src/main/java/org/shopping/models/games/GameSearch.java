package org.shopping.models.games;

import lombok.*;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class GameSearch {
    private int page = 1; // 페이지 번호
    private int limit = 20; // 1페이지당 레코드 갯수

    private String cateCd; // 분류 코드
    private Long gameNo; // 게임 번호
    private String sopt; // 검색 옵션
    private String skey; // 검색 키워드

    private String sort; // 정렬
}