package org.shopping.entities;

import jakarta.persistence.*;
import lombok.*;
import org.shopping.commons.constants.GameStatus;

import java.time.LocalDateTime;
import java.util.List;

@Entity @Data @Builder
@AllArgsConstructor @NoArgsConstructor
public class Game extends BaseEntity {
    @Id @GeneratedValue
    private Long gameNo;

    @ToString.Exclude
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="cateCd", nullable = false)
    private Category category;

    @Column(length=45, nullable = false)
    private String gid;

    @Column(length=100, nullable = false)
    private String gameNm;
    private int price;
    private int stock;

    @Enumerated(EnumType.STRING)
    @Column(length=25, nullable = false)
    private GameStatus status = GameStatus.READY;

    @Lob
    private String description;

    private long listOrder;

    @Transient
    private List<FileInfo> mainImages; // 상품 메인 이미지

    @Transient
    private List<FileInfo> listImages; // 목록 이미지

    @Transient
    private List<FileInfo> editorImages; // 에디터 이미지




}