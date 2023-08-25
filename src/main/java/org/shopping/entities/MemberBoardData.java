package org.shopping.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity @Data @Builder
@AllArgsConstructor @NoArgsConstructor
@Table(indexes={
        @Index(name="idx_boarddata_category", columnList = "category DESC"),
        @Index(name="idx_boarddata_createAt", columnList = "createdAt DESC")
})
public class MemberBoardData extends BaseEntity {
    /* 게시글 번호 */
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="bId")
    private Board board;

    @Column(length=65, nullable = false)
    private String gid = UUID.randomUUID().toString();

    /* 작성자 */
    @Column(length=40, nullable = false)
    private String poster;

    /* 비회원 비밀번호 */
    @Column(length=65)
    private String guestPw;

    /* 게시판 분류 */
    @Column(length=60)
    private String category;

    /* 제목 */
    @Column(nullable = false)
    private String subject;

    /* 내용 */
    @Lob
    @Column(nullable = false)
    private String content;

    /* 조회수 */
    private int hit;

    /* User-Agent : 브라우저 정보 */
    @Column(length=125)
    private String ua;

    /* 작성자 IP */
    @Column(length=20)
    private String ip;

    /* 댓글 수 */
    private int commentCnt;

    /* 작성 회원 */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="userNo")
    private Member member;
}