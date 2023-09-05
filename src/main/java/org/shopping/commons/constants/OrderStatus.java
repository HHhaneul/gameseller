package org.shopping.commons.constants;

import java.util.Arrays;
import java.util.List;

public enum OrderStatus {
    READY("주문접수"), // 주문접수
    INCASH("입금확인"), // 입금확인 
    PREPARE("상품준비중"), // 상품준비중
    DELIVERY("배송중"), // 배송중
    ARRIVAL("배송완료"), // 배송완료
    DONE("주문완료"), // 주문완료
    CANCEL("주문취소"), // 주문취소
    REFUND("환불"), // 환불 
    EXCHANGE("교환"); // 교환

    private String title;

    OrderStatus(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public static List<String[]> getList() {
        return Arrays.asList(
                new String[] {READY.name(), "주문접수"},
                new String[] {INCASH.name(), "입금확인"},
                new String[] {PREPARE.name(), "상품준비중"},
                new String[] {DELIVERY.name(), "배송중"},
                new String[] {ARRIVAL.name(), "배송완료"},
                new String[] {DONE.name(), "주문완료"},
                new String[] {CANCEL.name(), "주문취소"},
                new String[] {REFUND.name(), "환불"},
                new String[] {EXCHANGE.name(), "교환"}
        );

    }
}
