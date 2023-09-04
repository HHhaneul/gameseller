package org.shopping.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.shopping.commons.constants.PaymentType;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder @NoArgsConstructor @AllArgsConstructor
public class OrderInfo {

    @Id @GeneratedValue
    private Long orderNo;

    @Column(length=40, nullable = false)
    private String orderName;

    @Column(length=100, nullable = false)
    private String orderEmail;

    @Column(length=11, nullable = false)
    private String orderMobile;

    @Column(length=40, nullable = false)
    private String receiverName;

    @Column(length=11, nullable = false)
    private String receiverMobile;

    @Column(length=10, nullable = false)
    private String zonecode;

    @Column(length=100, nullable = false)
    private String address;

    @Column(length=100, nullable = false)
    private String addressSub;

    @Enumerated(EnumType.STRING)
    @Column(length=15, nullable = false)
    private PaymentType paymentType;

    private int payPrice; // 결제 금액

    @Column(length=50)
    private String deliveryCompany; // 배송 업체
    @Column(length=50)
    private String invoice; // 운송장 번호

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="userNo")
    private Member member;

    @Transient
    private String gameNm;

    @OneToMany(mappedBy = "orderInfo", fetch=FetchType.EAGER)
    private List<OrderItem> orderItems = new ArrayList<>();
}
