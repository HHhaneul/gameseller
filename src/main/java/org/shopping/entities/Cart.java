package org.shopping.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class Cart extends BaseEntity {

    /* 장바구니 */
    @Id @GeneratedValue
    private Long CartNo;

    /* 주문자명 */
    @Column(length=40, nullable = false)
    private String buyerNm;

    /* 주문한 상품 번호 */
    private List<Long> gameNo;

    /* 주문한 상품의 수량 */
    private Long buyerCnt;

    /* 사용자 연동 */
    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="userNo", nullable = false)
    private Member member;
}
