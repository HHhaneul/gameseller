package org.shopping.commons.constants;

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
}
