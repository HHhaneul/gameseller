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
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="bId")
    private Board board;

    @Column(length=65, nullable = false)
    private String gid = UUID.randomUUID().toString();

    @Column(length=40, nullable = false)
    private String poster;

    @Column(length=65)
    private String guestPw;

    @Column(length=60)
    private String category;

    @Column(nullable = false)
    private String subject;

    @Lob
    @Column(nullable = false)
    private String content;
    private int hit;

    @Column(length=125)
    private String ua;

    @Column(length=20)
    private String ip;

    private int commentCnt;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="userNo")
    private Member member;
}