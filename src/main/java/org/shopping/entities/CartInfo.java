package org.shopping.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class CartInfo extends BaseEntity {
    @Id @GeneratedValue
    private Long cartNo;

    @Column(name="_uid")
    private int uid;

    @Column(length=10, nullable = false, name="_mode")
    private String mode = "direct"; // 바로 구매

    private int ea = 1;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="gameNo")
    private Game game;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="userNo")
    private Member member;
}
