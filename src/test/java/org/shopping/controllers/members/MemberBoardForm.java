package org.shopping.controllers.members;

import jakarta.persistence.*;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.shopping.entities.Member;
import org.shopping.entities.MemberBoardData;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class MemberBoardForm {


    /* 게시글 번호 */
    @Id @GeneratedValue
    private Long id;

    private String mode;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="bId")
    private String bId;

    private String gid;

    /* 작성자 */
    private String poster;

    // 비회원 비밀번호
    private String guestPw;
    // 게시판 분류
    private String category;
    // 제목
    private String subject;
    // 내용
    private String content;
    // 조회수
    private int hit;
    // User-Agent : 브라우저 정보
    private String ua;
    // 작성자 IP
    private String ip;
    // 댓글 수
    private int commentCnt;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="userNo")
    private Member member; // 작성 회원

    public static MemberBoardData of(MemberBoardForm memberBoardForm) {
        return new ModelMapper().map(memberBoardForm, MemberBoardData.class);
    }
}
