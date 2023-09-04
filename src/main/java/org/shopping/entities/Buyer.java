package org.shopping.entities;

import jakarta.persistence.*;
import lombok.*;
import org.shopping.commons.constants.BuyerStatus;

import java.util.UUID;

@Entity
@Data @Builder
@NoArgsConstructor @AllArgsConstructor
@Table(indexes={
        @Index(name="idx_buyer_gid", columnList = "gid")
})
public class Buyer extends BaseMemberEntity {

    /* 주문번호 */
    @Id @GeneratedValue
    private Long buyerNo;

    @Column(length=45, nullable = false)
    private String gid = UUID.randomUUID().toString();

    /* 주문 완료 여부 */
    private boolean orderDone;

    /* 주문자명 */
    @Column(length=40, nullable = false)
    private String buyerNm;

    /* 주문한 상품의 수량 */
    private Long buyerCnt;

    /* 주문한 총 금액 */
    private Long sumPrice;

    @Enumerated(EnumType.STRING)
    @Column(length=25, nullable = false)
    private BuyerStatus status = BuyerStatus.ORDER;

    /* 운송장번호 */
    @Transient
    private String waybillNo;

}
