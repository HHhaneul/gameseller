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


    @Column(length=45, nullable = false)
    private String gid;

    @Column(length=100, nullable = false)
    private String gameNm;
    private int price;
    private int stock;

    @Enumerated(EnumType.STRING)
    @Column(length=25, nullable = false, name = "_status")
    private GameStatus status = GameStatus.READY;

    @Lob
    private String description;

    private long listOrder;

    /* 상품 메인 이미지 */
    @Transient
    private List<FileInfo> mainImages;

    /* 목록 이미지 */
    @Transient
    private List<FileInfo> listImages;

    /* 에디터 이미지 */
    @Transient
    private List<FileInfo> editorImages;

    /* 개발자 */
    private List<String> developer;

    /* 배급사 */
    private List<String> publisher;
    
    /* 출시일 */
    @Column(updatable = false)
    private LocalDateTime releaseDate;

    @ToString.Exclude
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="cateCd")
    private Category category;

}