package org.shopping.entities;

import jakarta.persistence.*;
import lombok.*;
import org.shopping.commons.constants.OrderStatus;

@Entity
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class OrderItem extends BaseMemberEntity {
    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Long cartNo;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="gameNo")
    private Game game;

    private String cateNm;

    @Column(length=100, nullable = false)
    private String gameNm;
    private int price;
    private int ea = 1;
    private int totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(length=15, nullable = false)
    private OrderStatus status = OrderStatus.READY;

    @Column(length=50)
    private String deliveryCompany; // 배송 업체
    @Column(length=50)
    private String invoice; // 운송장 번호

    @ToString.Exclude
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="orderNo")
    private OrderInfo orderInfo;
}
