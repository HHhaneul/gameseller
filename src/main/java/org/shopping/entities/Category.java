package org.shopping.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data @Builder
@NoArgsConstructor @AllArgsConstructor
@Table(indexes=@Index(name="idx_category_order", columnList = "listOrder DESC, createdAt"))
public class Category extends BaseMemberEntity {
    @Id
    @Column(length=30)
    private String cateCd;

    @Column(length=60, nullable = false)
    private String cateNm;

    @Column(name="_use")
    private boolean use;

    private long listOrder; // 진열 순서

    /** 도서 목록 조회 */
    @OneToMany(mappedBy="category", fetch=FetchType.LAZY)
    private List<Game> games = new ArrayList<>();

}