package org.shopping.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Data @Builder
@NoArgsConstructor @AllArgsConstructor
@Table(indexes={
        @Index(name="idx_buyer_gid", columnList = "gid")
})
public class Buyer extends BaseMemberEntity {

    /* 주문 번호 */
    @Id @GeneratedValue
    private Long buyerNo;

    @Column(length = 45, nullable = false)
    private String gid = UUID.randomUUID().toString();

    /* 주문 완료 여부 */
    private boolean orderDone;

    /* 주문자명 */
    @Column(length = 40, nullable = false)
    private String buyerNm;
    
    /* 주문 상품 개수 */
    private Long buyerCnt;
    
    /* 게임명 */
    private String GameNm;
    
    /* 주문한 상품 분류 */
    @Column(length = 60)
    private String category;
    
    /* 상품 개발자 */
    private String developer;
    
    /* 상품 가격 */
    private int price;

}
